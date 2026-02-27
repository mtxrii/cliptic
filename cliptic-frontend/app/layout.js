import "./globals.css";
import DayThemeCycle from "./day-theme-cycle";

export const metadata = {
  title: "Cliptic | URL Shortener",
  description: "Create shortened URLs with optional custom aliases.",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <DayThemeCycle />
        {children}
      </body>
    </html>
  );
}
