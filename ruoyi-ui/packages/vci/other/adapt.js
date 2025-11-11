// import { getElementBody, getElementHtml } from "@vci/helper/src/element";

export const viewport = {
  width: 3840,
  height: 1080
};
// export const adapt = (pkg, vm, adaptType = "vci", origin = viewport) => {
//   const pluginsPostcss = pkg?.postcss?.plugins;
//   let fitMode = "cover"; // contain: 留边适配; cover: 铺满屏幕（可能裁切）; fill: 拉伸填充（不裁切、可能变形）
//   if (pluginsPostcss) {
//     const pluginPostcssAdapt = pluginsPostcss["postcss-adapt-datav"];
//     pluginPostcssAdapt && pluginPostcssAdapt["viewportWidth"] && (viewport.width = pluginPostcssAdapt["viewportWidth"]);
//     pluginPostcssAdapt && pluginPostcssAdapt["viewportHeight"] && (viewport.height = pluginPostcssAdapt["viewportHeight"]);
//     // fit/scaleMode 优先
//     const fit = pluginPostcssAdapt && (pluginPostcssAdapt["fit"] || pluginPostcssAdapt["scaleMode"]);
//     if (fit && ["contain", "cover", "fill"].includes(fit)) fitMode = fit;
//     // 优先读取显式模式配置
//     const mode = pluginPostcssAdapt && (pluginPostcssAdapt["mode"] || pluginPostcssAdapt["adaptType"]);
//     if (mode && ["full", "fixed", "vci"].includes(mode)) adaptType = mode;
//     else if (pluginPostcssAdapt && Object.prototype.hasOwnProperty.call(pluginPostcssAdapt, "fixedSize")) {
//       if (pluginPostcssAdapt["fixedSize"] === true) adaptType = "fixed";
//       else if (pluginPostcssAdapt["fixedSize"] === false) adaptType = "full";
//     }
//   }
//   const elBody = getElementBody();
//   const elHtml = getElementHtml();
//   const el = vm.$el;
//   const originWidth = origin.width, originHeight = origin.height;
//   if (adaptType === "full") {
//     elHtml.style.height = elBody.style.height = "100%";
//     elHtml.style.width = elBody.style.width = "100%";
//     elBody.style.overflow = "hidden";
//     // 让挂载节点填满视口
//     el.style.width = "100%";
//     el.style.height = "100%";
//     // 缩放内部 .app，保持比例并居中
//     const elApp = el.querySelector(".app") || el;
//     elApp.style.width = `${originWidth}px`;
//     elApp.style.height = `${originHeight}px`;
//     elApp.style.transformOrigin = "0 0";
//     elApp.style.position = "absolute";
//     // 确保父容器可定位
//     if (elApp.parentElement && getComputedStyle(elApp.parentElement).position === "static") {
//       elApp.parentElement.style.position = "relative";
//     }
//     const resize = {
//       inter: -1,
//       fn: () => {
//         const sx = window.innerWidth / originWidth;
//         const sy = window.innerHeight / originHeight;
//         if (fitMode === "fill") {
//           // 拉伸填满，不裁切不留边（会变形）
//           elApp.style.transform = `translate(0px, 0px) scale(${sx}, ${sy})`;
//           return;
//         }
//         const s = fitMode === "cover" ? Math.max(sx, sy) : Math.min(sx, sy);
//         const offsetX = (window.innerWidth - originWidth * s) / 2;
//         const offsetY = (window.innerHeight - originHeight * s) / 2;
//         elApp.style.transform = `translate(${offsetX}px, ${offsetY}px) scale(${s})`;
//       }
//     };
//     // 初始化执行一次，避免首次空白
//     resize.fn();
//     window.addEventListener("resize", () => {
//       clearTimeout(resize.inter);
//       resize.inter = setTimeout(resize.fn, 50);
//     });
//   } else if (adaptType === "fixed") {
//     const elApp = el.querySelector(".app");
//     elApp.style.width = `${originWidth}px`;
//     elApp.style.height = `${originHeight}px`;
//     elBody.style.overflow = null;
//   } else if (adaptType === "vci") {
//     // 确保百分比高度链，避免非固定模式下塌缩
//     elHtml.style.height = elBody.style.height = "100%";
//     elHtml.style.width = elBody.style.width = "100%";
//     el.style.width = "100%";
//     el.style.height = "100%";
//     elBody.style.overflow = "hidden";
//   } else {
//     console.warn("未知的适配模式: " + adaptType);
//   }
// };
// export const toNumberType = arg => {
//   arg = Number(arg);
//   if (isNaN(arg)) return 0;
//   return arg;
// };
// export const toVw = (px, withUnit = true) => toNumberType(px) / (viewport.width / 100) + (withUnit ? "vw" : 0);
// export const toVh = (px, withUnit = true) => toNumberType(px) / (viewport.height / 100) + (withUnit ? "vh" : 0);
