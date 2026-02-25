'use client';

import { useState } from "react";
import { useRouter } from "next/navigation";

const API_URL = "/api/create-link";

function isValidUrl(value) {
  try {
    new URL(value);
    return true;
  } catch {
    return false;
  }
}

export default function Home() {
  const router = useRouter();
  const [url, setUrl] = useState("");
  const [customAlias, setCustomAlias] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");

    const trimmedUrl = url.trim();
    const trimmedAlias = customAlias.trim();

    if (!trimmedUrl) {
      setError("URL is required.");
      return;
    }

    if (!isValidUrl(trimmedUrl)) {
      setError("Please enter a valid URL including protocol (https://...).");
      return;
    }

    setIsSubmitting(true);

    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          originalUrl: trimmedUrl,
          alias: trimmedAlias || undefined,
        }),
      });

      const payload = await response.json();

      if (!response.ok) {
        throw new Error(payload?.message || "Unable to create shortened URL.");
      }

      const shortUrl = payload?.data?.shortUrl || payload?.shortUrl;
      if (!shortUrl) {
        throw new Error("API did not return a short URL.");
      }

      router.push(
        `/success?shortUrl=${encodeURIComponent(shortUrl)}&originalUrl=${encodeURIComponent(trimmedUrl)}`
      );
    } catch (requestError) {
      setError(requestError.message || "Something went wrong. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <main className="page-wrap">
      <section className="form-shell">
        <p className="eyebrow">Cliptic</p>
        <h1>Shorten a link</h1>
        <p className="subtitle">
          Paste your long URL, optionally set a custom alias, and generate a
          shareable short link. No signup required
        </p>

        <form onSubmit={handleSubmit} className="shortener-form">
          <label htmlFor="url">Destination URL</label>
          <input
            id="url"
            type="url"
            inputMode="url"
            placeholder="https://example.com/a-very-long-link"
            value={url}
            onChange={(event) => setUrl(event.target.value)}
            required
          />

          <label htmlFor="customAlias">
            Custom alias{" "}
            <span className="label-hint">(leave empty to auto-generate)</span>
          </label>
          <input
            id="customAlias"
            type="text"
            placeholder="my-campaign"
            value={customAlias}
            onChange={(event) => setCustomAlias(event.target.value)}
          />

          {error ? <p className="error-message">{error}</p> : null}

          <button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Creating..." : "Create short URL"}
          </button>
        </form>
      </section>
    </main>
  );
}
