import { redirect } from "next/navigation";

const BACKEND_REDIRECT_BASE_URL =
  process.env.SHORTENER_BACKEND_REDIRECT_BASE_URL || "http://localhost:8080";

function buildAliasUrl(alias) {
  const normalizedBase = BACKEND_REDIRECT_BASE_URL.replace(/\/+$/, "");
  return `${normalizedBase}/${encodeURIComponent(alias)}`;
}

export default async function AliasRedirectPage({ params }) {
  const alias = params?.alias;

  if (!alias) {
    redirect("/404");
  }

  const aliasUrl = buildAliasUrl(alias);

  const backendResponse = await fetch(aliasUrl, {
    method: "GET",
    redirect: "manual",
    cache: "no-store",
  });

  if (backendResponse.status === 404) {
    redirect("/404");
  }

  const location = backendResponse.headers.get("location");
  if (location && backendResponse.status >= 300 && backendResponse.status < 400) {
    redirect(location);
  }

  redirect(aliasUrl);
}
