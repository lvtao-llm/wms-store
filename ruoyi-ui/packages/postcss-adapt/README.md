# 可视化自适应方案(postcss插件)

### 描述

````
    插件作用于整个项目关联的CSS样式
    将编写的样式代码中px转换为vw,hpx转换为vh来
    并提供了uncoverSelectors,minPixelValue等参数进行更多定制化的转换规则。
````

### 用例

````
    width: 1920px  ->  width: 100vw;
    height: 1080hpx  ->  height: 100vh;
````

### 安装

````
    npm i -D @vci/adapt
````

### 默认配置

````
    {
        enabled: false              // true-启用 false-停用 
        viewportWidth: 1920,        // 设计稿宽度
        viewportHeight: 1080,       // 设计稿高度
        unitPrecision: 6,           // 转换后的数值小数点位数
        viewportUnitWidth: "vw",    // px转换后的单位
        viewportUnitHeight: "vh",   // hpx转换后的单位
        uncoverSelectors: [],       // 不进行转换的选择器
    }
````

### 配置方式

* 在postcss.config.js中:
    ````
    module.exports = {
              "plugins": {
                "postcss-adapt": {
                  "enabled": true,
                  "viewportWidth": 1920,
                  "viewportHeight": 1080,
                  "unitPrecision": 6,
                  "viewportUnitWidth": "vw",
                  "viewportUnitHeight": "vh",
                  "uncoverSelectors": []
                }
              }
            }
    ````
* 在package.json中:
  ````
    {
        "postcss": {
            "plugins": {
              "postcss-adapt": {
                  "enabled": true,
                  "viewportWidth": 1920,
                  "viewportHeight": 1080,
                  "unitPrecision": 6,
                  "viewportUnitWidth": "vw",
                  "viewportUnitHeight": "vh",
                  "uncoverSelectors": []
              }
            }
          }
    }
  ````