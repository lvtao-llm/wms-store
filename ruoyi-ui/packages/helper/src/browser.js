// 浏览器版本
import { addCss, getElementBody } from "./element";
import { mergeDeep } from "./object";
import { isEmpty } from "./other";

/**
 * 获取网址查询参数中的某一个参数值
 * @param nameSearch
 * @param searches
 * @returns {null|string}
 */
function getSearchFromLink(nameSearch, searches) {
  searches = searches || window.location.search;
  const reg = new RegExp(`(?<=${ name }=)[^&]*`);
  const match = searches.match(reg);
  if (match && match.length > 0) return window.decodeURIComponent(match[0]);
  else return null;
}
// 获取浏览器版本
function getBrowserVersion() {
  const userAgent = window.navigator.userAgent;
  let version = "未知浏览器";
  const fireFox = userAgent.match(/Firefox\/[\d+.\d+]+/);
  if (fireFox) {
    version = `火狐浏览器 版本: ${fireFox[0].substring(fireFox[0].indexOf("/") + 1, fireFox[0].length)}`;
  } else {
    const chrome = userAgent.match(/Chrome\/[\d+.\d+]+/);
    if (chrome) {
      version = `谷歌浏览器 版本: ${chrome[0].substring(chrome[0].indexOf("/") + 1, chrome[0].length)}`;
      if (window.navigator.mimeTypes[0] && window.navigator.mimeTypes[0].type.indexOf("360") !== -1)
        version = `360浏览器 版本: ${chrome[0].substring(chrome[0].indexOf("/") + 1, chrome[0].length)}`;
      let edge = userAgent.match(/Edge\/[\d+.\d+]+/);
      if (edge) version = `IE-Eege浏览器 版本: ${edge[0].substring(edge[0].indexOf("/") + 1, edge[0].length)}`;
      let qq = userAgent.match(/QQBrowser\/[\d+.\d+]+/);
      if (qq) version = `QQ浏览器 版本: ${qq[0].substring(qq[0].indexOf("/") + 1, qq[0].length)}`;
    } else {
      const edge = userAgent.match(/rv:11.0/);
      if (edge) version = "IE11浏览器";
      else if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1) version = "IE10及一下版本浏览器";
    }
  }
  return version;
}
// 浏览器客户端信息
function client() {
  const agent = navigator.userAgent.toLowerCase();
  const ts = {
    isIpad: /ipad/.test(agent),
    isIphoneOs: /iphone\sos/.test(agent),
    isMidp: /midp/.test(agent),
    isUc7: /rv:1.2.3.4/.test(agent),
    isUc: /ucweb/.test(agent),
    isAndroid: /android/.test(agent),
    isWindowCe: /window\sce/.test(agent),
    isWindowPhone: /windows\smobile/.test(agent)
  };
  ts.isMobile = ts.isIpad || ts.isIphoneOs || ts.isMidp || ts.isUc7 || ts.isUc || ts.isAndroid || ts.isWindowCe || ts.isWindowPhone;
  ts.isPC = !ts.isMobile;
  ts.version = getBrowserVersion();
  return ts;
}
/**
 * 复制文本内容
 * @param text
 * @returns {Promise<boolean>}
 */
function copyText(text) {
  return new Promise(resolve => {
    const body = getElementBody();
    const elCopy = document.createElement("input");
    body.insertAdjacentElement("beforeend", elCopy);
    elCopy.setAttribute("value", text);
    elCopy.select();
    // TODO 2024年后用Clipboard替代
    document.execCommand && document.execCommand("copy");
    body.removeChild(elCopy);
    resolve(true);
  });
}
/**
 * 下载文件
 * @param data        要下载的数据
 * @param fileName    下载的文件名
 * @param option
 */
function downloadData(data, fileName, option) {
  const { isUrl, isBlob, enableStringify, stringifyReplacer, stringifySpace, blob } = mergeDeep({
    isUrl: false,
    isBlob: false,
    // 是否进行JSON.stringify
    enableStringify: true,
    // 如果进行stringify，设置格式化 2个空格
    stringifySpace: 2,
    // 如果进行stringify，设置stringifyReplacer
    stringifyReplacer: null,
    // 创建Blob时的配置
    blob: {}
  }, option);
  let url;
  if (isBlob) {
    url = URL.createObjectURL(data);
  } else if (isUrl) {
    url = data;
  } else {
    if (enableStringify) {
      try {
        data = JSON.stringify(data, stringifyReplacer, stringifySpace);
      } catch (e) {
        console.warn(e);
      }
    }
    url = URL.createObjectURL(new Blob([data], blob));
  }
  const el = document.createElement("a");
  el.download = fileName || "文件.json";
  el.href = url;
  addCss(el, { display: "none" });
  const body = getElementBody();
  body.appendChild(el);
  el.click();
  body.removeChild(el);
  URL.revokeObjectURL(url);
}
/**
 * 清除定时器
 * @param scheduledTask  定时器ID
 */
function clearScheduledTask(scheduledTask) {
  try {
    clearInterval(scheduledTask);
    clearTimeout(scheduledTask);
  } catch (e) {
    //
  }
}
/**
 * 清除scheduledTasks中的所有定时器
 * @param scheduledTasks
 */
function clearScheduledTasks(scheduledTasks) {
  if (!isEmpty(scheduledTasks)) {
    if (typeof scheduledTasks === "object") Object.entries(scheduledTasks).forEach(([, value]) => clearScheduledTasks(value));
    else clearScheduledTask(scheduledTasks);
  }
}
// 获取当前时间戳
function now() {
  return (typeof performance === "undefined" ? Date : performance).now(); // see #10732
}
// 全屏|进入
function fullScreenBoot() {
  const element = document.documentElement;
  if (element.requestFullscreen) {
    element.requestFullscreen();
  } else if (element.webkitRequestFullscreen) { /* Safari */
    element.webkitRequestFullscreen();
  } else if (element.msRequestFullscreen) { /* IE11 */
    element.msRequestFullscreen();
  }
}
// 全屏|退出
function fullScreenExit() {
  if (document.exitFullscreen) {
    document.exitFullscreen();
  } else if (document.webkitExitFullscreen) { /* Safari */
    document.webkitExitFullscreen();
  } else if (document.msExitFullscreen) { /* IE11 */
    document.msExitFullscreen();
  }
}
// 全屏|判断是否进入
function isFullScreenBoot() {
  return !!(document.fullscreenElement || document.webkitFullscreenElement || document.msFullscreenElement);
}
export {
  getSearchFromLink,
  getBrowserVersion,
  client,
  copyText,
  downloadData,
  clearScheduledTask,
  clearScheduledTasks,
  now,
  fullScreenBoot,
  fullScreenExit,
  isFullScreenBoot
};