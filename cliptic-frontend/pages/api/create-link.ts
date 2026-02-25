import { NextApiRequest, NextApiResponse } from "next";

const BACKEND_CREATE_URL =
  process.env.SHORTENER_BACKEND_CREATE_URL || "http://localhost:8080/url";
const BACKEND_AUTH_TOKEN = process.env.SHORTENER_BACKEND_TOKEN;
const BACKEND_AUTH_HEADER = process.env.SHORTENER_BACKEND_AUTH_HEADER;

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

export default async function CreateLink(
  request: NextApiRequest,
  response: NextApiResponse
) {
  if (request.method !== "POST") {
    return response.status(405).json({
      type: "Error",
      code: 405,
      message: "Only POST method is accepted on this route",
    });
  }

  const authorizationHeader = buildAuthorizationHeader();
  const authDebug = `auth_set=${authorizationHeader ? "yes" : "no"},auth_prefix=${authorizationHeader.split(" ")[0] || "none"},auth_len=${authorizationHeader.length}`;

  if (!authorizationHeader) {
    return response.status(500).json({
      type: "Error",
      code: 500,
      message:
        "Missing auth config. Set SHORTENER_BACKEND_TOKEN or SHORTENER_BACKEND_AUTH_HEADER.",
    });
  }

  const { originalUrl, alias, link } = request.body ?? {};
  const normalizedOriginalUrl = (originalUrl || link || "").trim();
  const normalizedAlias = (alias || "").trim();

  if (!normalizedOriginalUrl) {
    response.status(400).json({
      type: "Error",
      code: 400,
      message: "Expected { originalUrl: string, alias?: string }",
    });
    return;
  }

  try {
    const backendResponse = await fetch(BACKEND_CREATE_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: authorizationHeader,
      },
      body: JSON.stringify({
        originalUrl: normalizedOriginalUrl,
        alias: normalizedAlias,
      }),
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
        "Backend request failed while creating shortened URL.";

      const location = backendResponse.headers.get("location");
      const redirectNote = location ? ` Redirect location: ${location}` : "";
      return response.status(backendResponse.status).json({
        type: "Error",
        code: backendResponse.status,
        message: `Backend ${backendResponse.status}: ${backendMessage}.${redirectNote} (${authDebug})`,
      });
    }

    const shortUrl =
      normalizedPayload?.data?.shortUrl ||
      normalizedPayload?.shortUrl ||
      normalizedPayload?.data?.shortenedUrl ||
      normalizedPayload?.shortenedUrl ||
      normalizedPayload?.data?.url ||
      normalizedPayload?.url;

    if (!shortUrl) {
      return response.status(502).json({
        type: "Error",
        code: 502,
        message: "Backend did not return a short URL.",
      });
    }

    return response.status(201).json({
      type: "success",
      code: 201,
      data: {
        shortUrl,
        originalUrl: normalizedOriginalUrl,
      },
    });
  } catch (e: any) {
    return response.status(500).json({
      code: 500,
      type: "error",
      message: e.message,
    });
  }
}
