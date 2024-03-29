const express = require('express');
const { MongoClient } = require("mongodb");
const { listLinks } = require("./mongodb");

const app = express();
const port = 3000;

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})

app.get('/mongodb', async (req, res) => {
  const uri = 'mongodb://localhost:27017/'

  const client = new MongoClient(uri);
  const db = client.db('cliptic');

  await listLinks(db);
  res.send('done');
})