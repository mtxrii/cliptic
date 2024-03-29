export const saveLink = async (db, url) => {
  const linkCollection = db.collection("link");

  const doc = { url: url, id: "placeholder uuid"};
  const result = await linkCollection.insertOne(doc);
}

export const getLink = async (db, id) => {
  try {
    const linkCollection = db.collection("link");

    const query = { id: id };
    const link = await linkCollection.findOne(query);
  } finally {
    await client.close();
  }
}

export const listLinks = async (db) => {
  const linkCollection = db.collection("link");

  const all = await db.collection("link").find({});
  console.log(all);
}