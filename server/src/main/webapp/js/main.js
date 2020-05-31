function openLink(newWindow, url) {
  if (newWindow) {
    return !window.open(url);
  } else {
    window.location = url;
  }
}