import { redirect } from "next/navigation";

export default function AliasRedirectPage({ params }) {
  const alias = params?.alias;

  if (!alias) {
    redirect("/");
  }

  redirect(`http://localhost:8080/${encodeURIComponent(alias)}`);
}
