import { NextApiRequest, NextApiResponse } from "next";

const BACKEND_INFO_URL = process.env.SHORTENER_BACKEND_INFO_URL || "http://localhost:8080/url";
const BACKEND_AUTH_TOKEN = process.env.SHORTENER_BACKEND_TOKEN;
const BACKEND_AUTH_HEADER = process.env.SHORTENER_BACKEND_AUTH_HEADER;
const HARDCODED_PASSCODE = "your-passcode";

function buildAuthorizationHeader() {
  if (BACKEND_AUTH_HEADER && BACKEND_AUTH_HEADER.trim()) {
    return BACKEND_AUTH_HEADER.trim();
  }

  const token = BACKEND_AUTH_TOKEN?.trim();
  if (!token) {
    return "";
  }

  if (/^Bearer\s+/i.test(token)) {
    return token;
  }

  return `Bearer ${token}`;
}

export default async function LinkInfo(
  request: NextApiRequest,
  response: NextApiResponse
) {
  if (request.method !== "GET") {
    return response.status(405).json({
      type: "Error",
      code: 405,
      message: "Only GET method is accepted on this route",
    });
  }

  const authorizationHeader = buildAuthorizationHeader();
  if (!authorizationHeader) {
    return response.status(500).json({
      type: "Error",
      code: 500,
      message:
        "Missing auth config. Set SHORTENER_BACKEND_TOKEN or SHORTENER_BACKEND_AUTH_HEADER.",
    });
  }

  const alias = String(request.query.alias || "").trim();
  if (!alias) {
    return response.status(400).json({
      type: "Error",
      code: 400,
      message: "Missing required query parameter: alias",
    });
  }

  try {
    const url = new URL(BACKEND_INFO_URL);
    url.searchParams.set("alias", alias);
    url.searchParams.set("passcode", HARDCODED_PASSCODE);

    const backendResponse = await fetch(url.toString(), {
      method: "GET",
      headers: {
        Authorization: authorizationHeader,
      },
    });

    const rawBody = await backendResponse.text();
    let normalizedPayload: Record<string, any> = {};

    if (rawBody) {
      try {
        normalizedPayload = JSON.parse(rawBody) as Record<string, any>;
      } catch {
        normalizedPayload = {};
      }
    }

    if (!backendResponse.ok) {
      const backendMessage =
        normalizedPayload?.message ||
        normalizedPayload?.error ||
        rawBody ||
        "Backend request failed while fetching link info.";

      return response.status(backendResponse.status).json({
        type: "Error",
        code: backendResponse.status,
        message: `Backend ${backendResponse.status}: ${backendMessage}`,
      });
    }

    return response.status(200).json(normalizedPayload);
  } catch (e: any) {
    return response.status(500).json({
      code: 500,
      type: "error",
      message: e.message,
    });
  }
}
