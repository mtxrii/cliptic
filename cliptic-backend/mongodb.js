const mongoDbConfig = require('./config/mongodb.json');

const saveLink = async (db, url) => {
  const linkCollection = db.collection(mongoDbConfig.collection);

  const doc = { url: url, id: "placeholder uuid"};
  const result = await linkCollection.insertOne(doc);
}

const getLink = async (db, id) => {
  try {
    const linkCollection = db.collection(mongoDbConfig.collection);

    const query = { id: id };
    const link = await linkCollection.findOne(query);
  } finally {
    await client.close();
  }
}

const listLinks = async (db) => {
  const linkCollection = db.collection(mongoDbConfig.collection);

  const all = await linkCollection.find({});
  console.log(all);
}

module.exports = {
  saveLink,
  getLink,
  listLinks
}