"use client";

import { useState } from "react";

const REDIRECT_BASE_URL = "sample.com";
const INFO_URL = "/api/link-info";

export default function LinkInfoPage({ params }) {
  const alias = params?.alias || "";
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [urlInfo, setUrlInfo] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setIsSubmitting(true);

    try {
      const response = await fetch(
        `${INFO_URL}?alias=${encodeURIComponent(alias)}`,
        { method: "GET" }
      );

      const payload = await response.json();

      if (!response.ok) {
        throw new Error(payload?.message || "Unable to fetch link info.");
      }

      const firstUrl = payload?.urls?.[0];
      if (!firstUrl) {
        throw new Error("No URL info found for this alias.");
      }

      setUrlInfo(firstUrl);
      setPassword("");
    } catch (requestError) {
      setError(requestError.message || "Something went wrong.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <main className="page-wrap">
      <section className="form-shell">
        <p className="eyebrow">Cliptic</p>
        <h1>link info</h1>

        {urlInfo ? (
          <div className="result-panel">
            <div className="result-item">
              <p className="result-label">Alias</p>
              <p className="result-original">{urlInfo.alias}</p>
            </div>

            <div className="result-item">
              <p className="result-label">Is Custom Alias</p>
              <p className="result-original">{String(urlInfo.isCustomAlias)}</p>
            </div>

            <div className="result-item">
              <p className="result-label">Original URL</p>
              <a
                href={urlInfo.originalUrl}
                target="_blank"
                rel="noreferrer"
                className="result-link"
              >
                {urlInfo.originalUrl}
              </a>
            </div>

            <div className="result-item">
              <p className="result-label">Redirect URL</p>
              <a
                href={urlInfo.redirectUrl}
                target="_blank"
                rel="noreferrer"
                className="result-link"
              >
                {urlInfo.redirectUrl}
              </a>
            </div>

            <div className="result-item">
              <p className="result-label">Created By</p>
              <p className="result-original">{urlInfo.createdBy || "Unknown"}</p>
            </div>
          </div>
        ) : (
          <>
            <p className="subtitle">
              Enter password to view details for{" "}
              <a
                style={{ textDecoration: "underline" }}
                href={`${REDIRECT_BASE_URL}/${alias}`}
                target="_blank"
                rel="noopener noreferrer"
              >
                {REDIRECT_BASE_URL}/{alias}
              </a>
              .
            </p>

            <form onSubmit={handleSubmit} className="shortener-form" noValidate>
              <label htmlFor="password">Password</label>
              <input
                id="password"
                type="password"
                autoComplete="current-password"
                placeholder="Enter password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
              />
              {error ? <p className="error-message">{error}</p> : null}
              <button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Loading..." : "View info"}
              </button>
            </form>
          </>
        )}
      </section>
    </main>
  );
}
