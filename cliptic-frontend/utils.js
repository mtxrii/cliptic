import { customAlphabet } from "nanoid";

const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

export default function formatLog(logStr) {
    let date = new Date().toISOString();
    date = date.replaceAll('-', '/')
               .replace('T', ' ')
               .replace('Z', '');
    return '[' + date + '] ' + logStr;
}

export default function getRandomHash(length) {
    return customAlphabet(characters, length);
}