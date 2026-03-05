import Link from "next/link";

export const metadata = {
  title: "404 | Cliptic",
  description: "No URL exists at this endpoint.",
};

export default function NotFoundPage() {
  return (
    <main className="page-wrap">
      <section className="form-shell">
        <p className="eyebrow">404</p>
        <h1>Page not found</h1>
        <p className="subtitle">No URL exists at this endpoint.</p>
        <p className="subtitle">
          <Link href="/">Return to the home page</Link>
        </p>
      </section>
    </main>
  );
}
