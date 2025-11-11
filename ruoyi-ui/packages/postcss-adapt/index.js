"use strict";
const postcss = require("postcss");
const wpxRegex = /"[^"]+"|'[^']+'|url\([^\\)]+\)|(\d*\.?\d+)(px)/ig;
const hpxRegex = /"[^"]+"|'[^']+'|url\([^\\)]+\)|(\d*\.?\d+)(hpx|\shpx)/ig;
const GlobalOptions = {
  enabled: true,
  viewportWidth: 1920,
  viewportHeight: 1080,
  unitPrecision: 6,
  viewportUnitWidth: "vw",
  viewportUnitHeight: "vh",
  ignoreSelectors: []
};

/**
 * 单位转换函数生成
 * @param viewportSize      设计稿尺寸
 * @param unitPrecision     转换后小数点位数
 * @param viewportUnit      转换后的单位
 * @returns {Function}
 */
function createPxReplace(viewportSize, unitPrecision, viewportUnit) {
  return function (m, $1) {
    if (!$1) return m;
    else {
      const value = parseFloat($1);
      if (value <= 1) return m;
      else return (parseFloat($1) / (viewportSize / 100)).toFixed(unitPrecision) + viewportUnit;
    }
  };
}

module.exports = postcss.plugin("vci-adapt", function (options) {
  // 编译配置
  const opts = Object.assign({}, GlobalOptions, options);
  // 单位替换函数 宽度
  const wpxReplacer = createPxReplace(opts.viewportWidth, opts.unitPrecision, opts.viewportUnitWidth);
  // 单位替换函数 高度
  const hpxReplacer = createPxReplace(opts.viewportHeight, opts.unitPrecision, opts.viewportUnitHeight);

  return function (root) {
    root.walkDecls(function (decl) {
      if (opts.enabled) {
        if (["px", "hpx"].some(key => decl.value.includes(key))) {
          if (!opts.ignoreSelectors.some(selector => {
            if (typeof selector === "string") return decl.parent["selector"].includes(selector);
            else return decl.parent["selector"].match(selector);
          })) decl.value = decl.value.replace(wpxRegex, wpxReplacer).replace(hpxRegex, hpxReplacer);
        }
      } else {
        if (decl.value.indexOf("hpx") !== -1) decl.value = decl.value.replace(hpxRegex, function (value, matched) {
          if (!matched) return value;
          return parseFloat(value) + "px";
        });
      }
    });
  };
});
