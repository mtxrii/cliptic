import Link from "next/link";

export const metadata = {
  title: "About | Cliptic",
  description: "Learn what Cliptic does and how to use it.",
};

export default function AboutPage() {
  return (
    <main className="page-wrap">
      <section className="form-shell">
        <p className="eyebrow">About Cliptic</p>
        <h1>Simple URL shortening</h1>
        <p className="subtitle">
          Cliptic turns long links into shorter, shareable URLs with optional
          custom aliases.
        </p>

        <div className="about-content">
          <p>
            Paste a destination URL on the home page, optionally add your own
            alias, and Cliptic returns a shortened link you can copy and share.
          </p>
          <p>What you can do right now:</p>
          <ul>
            <li>Create short URLs with generated hashes.</li>
            <li>Use a custom alias when you want a readable path.</li>
            <li>Copy the final URL from the success screen in one click.</li>
          </ul>
          <Link href="/" className="secondary-action">
            Back to shortener
          </Link>
        </div>
      </section>
    </main>
  );
}
