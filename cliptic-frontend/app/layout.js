import "./globals.css";
import DayThemeCycle from "./day-theme-cycle";
import Link from "next/link";

export const metadata = {
  title: "Cliptic | URL Shortener",
  description: "Create shortened URLs with optional custom aliases.",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <DayThemeCycle />
        <header className="top-nav">
          <div className="top-nav__inner">
            <Link href="/" className="top-nav__brand">
              Cliptic
            </Link>
            <nav className="top-nav__links" aria-label="Primary">
              <Link href="/" className="top-nav__link">
                Home
              </Link>
              <Link href="/success" className="top-nav__link">
                Success
              </Link>
            </nav>
          </div>
        </header>
        {children}
      </body>
    </html>
  );
}
