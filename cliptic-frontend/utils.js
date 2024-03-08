export default function formatLog(logStr) {
    let date = new Date().toISOString();
    date = date.replaceAll('-', '/')
               .replace('T', ' ')
               .replace('Z', '');
    return '[' + date + '] ' + logStr;
}