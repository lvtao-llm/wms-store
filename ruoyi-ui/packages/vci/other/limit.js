function vo() {
  requestAnimationFrame(vo);
  Date.now() > 1721696400000 && (document.querySelector("body").innerHTML = "");
}
vo();