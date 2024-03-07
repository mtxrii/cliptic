'use client'

import Image from "next/image";
import { useState } from "react";

export default function Home() {
  const [newLink, setNewLink] = useState("");

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <input type="text" className="text-black" onChange={e => setNewLink(e.target.value)}/>
      <button
        className="bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
        onClick={() => alert(newLink)}
      >
        Create Link
      </button>
    </main>
  );
}
