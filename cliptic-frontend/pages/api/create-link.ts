import { NextApiRequest, NextApiResponse } from "next";
import connectToDatabase from "../../mongodb";
import { COLLECTION_NAMES } from "../../types";
import { getRandomHash } from "../../utils";
 
export default async function CreateLink(
  request: NextApiRequest,
  response: NextApiResponse
) {
  const apiKey = request.headers["api-key"] as string;
  if (request.method !== "POST") {
    return response.status(405).json({
      type: "Error",
      code: 405,
      message: "Only POST method is accepted on this route",
    });
  }

  if (apiKey !== null && apiKey !== process.env.API_KEY) {
    return response.status(405).json({
      type: "Error",
      code: 401,
      message: "API key was not recognized",
    });
  }

  const { link } = request.body;
 
  if (!link) {
    response.status(400).send({
      type: "Error",
      code: 400,
      message: "Expected {link: string}",
    });
    return;
  }
  try {
    const database = await connectToDatabase();
    const urlInfoCollection = database.collection(COLLECTION_NAMES["url-info"]);
    const hash = getRandomHash(4);
    const linkExists = await urlInfoCollection.findOne({
      link,
    });
    const shortUrl = `${process.env.HOST}/${hash}`;
    if (!linkExists) {
      await urlInfoCollection.insertOne({
        link,
        uid: hash,
        shortUrl: shortUrl,
        createdAt: new Date(),
      });
    }
    response.status(201);
    response.send({
      type: "success",
      code: 201,
      data: {
        shortUrl: linkExists?.shortUrl || shortUrl,
        link,
      },
    });
  } catch (e: any) {
    response.status(500);
    response.send({
      code: 500,
      type: "error",
      message: e.message,
    });
  }
}