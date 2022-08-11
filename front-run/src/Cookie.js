function getCookie(name) {
  if (!document.cookie) {
    return;
  }
  const cookie = document.cookie
    .split("; ")
    .map((cookie) => cookie.split("="))
    .filter((cookie) => cookie[0] === name);
  return cookie[0][1];
}

export default getCookie;
