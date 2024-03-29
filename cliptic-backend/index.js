const express = require('express');
const { MongoClient } = require("mongodb");
const { listLinks } = require("./mongodb");
const mongoDbConfig = require('./config/mongodb.json');

const app = express();
const port = 3000;

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})

app.get('/mongodb', async (req, res) => {
  const client = new MongoClient(mongoDbConfig.uri.dev);
  const db = client.db('cliptic');

  await listLinks(db);
  res.send('done');
})