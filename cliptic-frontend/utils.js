import { customAlphabet } from "nanoid";

const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

export function formatLog(logStr) {
    let date = new Date().toISOString();
    date = date.replaceAll('-', '/')
               .replace('T', ' ')
               .replace('Z', '');
    return '[' + date + '] ' + logStr;
}

export function getRandomHash(length) {
    return customAlphabet(characters, length);
}