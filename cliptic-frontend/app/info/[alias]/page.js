const REDIRECT_BASE_URL = "sample.com";

export default function LinkInfoPage({ params }) {
  const alias = params?.alias || "";

  return (
    <main className="page-wrap">
      <section className="form-shell">
        <p className="eyebrow">Cliptic</p>
        <h1>link info</h1>
        <p className="subtitle">Enter password to view details for <a style={{textDecoration: 'underline'}} href={`${REDIRECT_BASE_URL}/${alias}`} target="_blank" rel="noopener noreferrer">{REDIRECT_BASE_URL}/{alias}</a>.</p>

        <form className="shortener-form" noValidate>
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            autoComplete="current-password"
            placeholder="Enter password"
          />
        </form>
      </section>
    </main>
  );
}
