'use client';

import { Suspense, useMemo, useState } from "react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

function SuccessPageContent() {
  const params = useSearchParams();
  const [copied, setCopied] = useState(false);

  const shortUrl = useMemo(() => params.get("shortUrl") || "", [params]);
  const originalUrl = useMemo(() => params.get("originalUrl") || "", [params]);

  const handleCopy = async () => {
    if (!shortUrl || !navigator?.clipboard) {
      return;
    }

    try {
      await navigator.clipboard.writeText(shortUrl);
      setCopied(true);
      setTimeout(() => setCopied(false), 1500);
    } catch {
      setCopied(false);
    }
  };

  return (
    <main className="page-wrap">
      <section className="form-shell success-shell">
        <p className="eyebrow">Success</p>
        <h1>Your shortened URL is ready</h1>
        <p className="subtitle">
          Share it anywhere. You can always create another one from the home
          page.
        </p>

        <div className="result-panel">
          <p className="result-label">Short URL</p>
          {shortUrl ? (
            <a
              href={shortUrl}
              target="_blank"
              rel="noreferrer"
              className="result-link"
            >
              {shortUrl}
            </a>
          ) : (
            <p className="error-message">No short URL found in this session.</p>
          )}

          {originalUrl ? (
            <>
              <p className="result-label">Original URL</p>
              <p className="result-original">{originalUrl}</p>
            </>
          ) : null}
        </div>

        <div className="success-actions">
          <button type="button" onClick={handleCopy} disabled={!shortUrl}>
            {copied ? "Copied" : "Copy URL"}
          </button>
          <Link href="/" className="secondary-action">
            Create another
          </Link>
        </div>
      </section>
    </main>
  );
}

export default function SuccessPage() {
  return (
    <Suspense
      fallback={
        <main className="page-wrap">
          <section className="form-shell success-shell">
            <p className="eyebrow">Success</p>
            <h1>Loading your shortened URL...</h1>
          </section>
        </main>
      }
    >
      <SuccessPageContent />
    </Suspense>
  );
}
