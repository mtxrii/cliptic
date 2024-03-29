// read sample from: https://www.mongodb.com/docs/drivers/node/current/quick-start/connect-to-mongodb/
const { MongoClient } = require("mongodb");
// Replace the uri string with your connection string.
const uri = "<connection string uri>";
const client = new MongoClient(uri);
async function run() {
  try {
    const database = client.db('sample_mflix');
    const movies = database.collection('movies');
    // Query for a movie that has the title 'Back to the Future'
    const query = { title: 'Back to the Future' };
    const movie = await movies.findOne(query);
    console.log(movie);
  } finally {
    // Ensures that the client will close when you finish/error
    await client.close();
  }
}
run().catch(console.dir);


// write sample from: https://www.mongodb.com/docs/drivers/node/current/fundamentals/crud/write-operations/insert/
const myDB = client.db("myDB");
const myColl = myDB.collection("pizzaMenu");

const doc = { name: "Neapolitan pizza", shape: "round" };
const result = await myColl.insertOne(doc);
console.log(
   `A document was inserted with the _id: ${result.insertedId}`,
);

const saveLink = async (db, url) => {
  const linkCollection = db.collection("link");

  const doc = { url: url, id: "placeholder uuid"};
  const result = await linkCollection.insertOne(doc);
}

const getLink = async (db, id) => {
  try {
    const linkCollection = db.collection("link");

    const query = { id: id };
    const link = await linkCollection.findOne(query);
  } finally {
    await client.close();
  }
}