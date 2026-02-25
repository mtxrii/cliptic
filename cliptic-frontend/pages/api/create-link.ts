import { NextApiRequest, NextApiResponse } from "next";

const BACKEND_CREATE_URL =
  process.env.SHORTENER_BACKEND_CREATE_URL || "http://localhost:8080/url";
const BACKEND_AUTH_TOKEN = process.env.SHORTENER_BACKEND_TOKEN;

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

  if (!BACKEND_AUTH_TOKEN) {
    return response.status(500).json({
      type: "Error",
      code: 500,
      message: "Missing SHORTENER_BACKEND_TOKEN in environment variables.",
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
        Authorization: `Bearer ${BACKEND_AUTH_TOKEN}`,
      },
      body: JSON.stringify({
        originalUrl: normalizedOriginalUrl,
        alias: normalizedAlias || undefined,
      }),
    });

    const backendPayload = await backendResponse
      .json()
      .catch(() => ({} as Record<string, unknown>));
    const normalizedPayload = backendPayload as Record<string, any>;

    if (!backendResponse.ok) {
      return response.status(backendResponse.status).json({
        type: "Error",
        code: backendResponse.status,
        message:
          normalizedPayload?.message ||
          "Backend request failed while creating shortened URL.",
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
