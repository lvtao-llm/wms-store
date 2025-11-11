import { trim } from "./string";

/**
 * 十六进制颜色转rgba
 * @param hex     十六进制颜色
 * @param alpha     转换后的透明度 [0, 1]
 * @returns {string}
 */
function hexToRgba(hex, alpha = 1) {
  if (!/^#([0-9a-f]{3}|[0-9a-f]{6})$/i.test(hex)) throw new Error("Invalid hex color value");
  hex = hex.replace(/^#/g, "");
  hex.length === 3 && (hex = hex.replace(/([0-9a-f])/gi, "$1$1"));
  const r = parseInt(hex.substring(0, 2), 16);
  const g = parseInt(hex.substring(2, 4), 16);
  const b = parseInt(hex.substring(4, 6), 16);
  return `rgba(${r}, ${g}, ${b}, ${alpha})`;
}
/**
 * rgba颜色转十六进制
 * @param rgba     rgba颜色
 * @returns {string}
 */
function rgbaToHex(rgba) {
  if (!/^rgba\(/.test(rgba)) throw new Error("Invalid rgba color value");
  rgba = trim(rgba).replace(/^rgba\(/g, "").replace(/\)/g, "");
  return "#" + rgba.split(",").map(v => (+v).toString(16)).join("");
}

/**
 * rgb颜色转十六进制
 * @param rgb     rgba颜色
 * @returns {string}
 */
function rgbToHex(rgb) {
  if (!/^rgb\(/.test(rgb)) throw new Error("Invalid rgb color value");
  rgb = trim(rgb).replace(/^rgb\(/g, "").replace(/\)/g, "");
  return "#" + rgb.split(",").map(v => (+v).toString(16)).map(v => v.length === 1 ? `0${v}` : v).join("");
}
export {
  hexToRgba,
  rgbaToHex,
  rgbToHex
};