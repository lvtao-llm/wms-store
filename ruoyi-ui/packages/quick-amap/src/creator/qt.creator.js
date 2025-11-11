import { mergeDeep } from "@vci/helper/src/object";
import QuickThree from "@vci/quick-three";
import { addCss } from "@vci/helper/src/element";
import { update } from "@tweenjs/tween.js";
import { PluginGui } from "@vci/quick-three/src/plugins/PluginGui";
import { PluginAnimation } from "@vci/quick-three/src/plugins/PluginAnimation";
import { PluginRenderer } from "@vci/quick-three/src/plugins/PluginRenderer";
import { PluginAdapt } from "@vci/quick-three/src/plugins/PluginAdapt";
import { PluginEvents } from "@vci/quick-three/src/plugins/PluginEvents";
import { PluginEdit } from "@vci/quick-three/src/plugins/PluginEdit";
import { PluginFns } from "@vci/quick-three/src/plugins/PluginFns";
import { PluginEffectComposer } from "@vci/quick-three/src/plugins/PluginEffectComposer";

export const QuickThreeCreatorInsertAdjacent = {
  Before: "before",
  After: "after"
};
export function QuickThreeCreator(map, AMap, option) {
  option = mergeDeep({
    // 插入到目标layer元素，不传则是地图默认图层元素
    insertTarget: null,
    // 插入在目标元素之前还是之后
    insertAdjacent: QuickThreeCreatorInsertAdjacent.After,
    zIndexAMap: 1,
    zIndex: 2,
    useGLCustomLayer: false,
    // 偏移量
    offsetOfLnglat: [0, 0],
    optionQt: null
  }, option);
  return new Promise(resolve => {
    const { insertTarget, insertAdjacent, optionQt, useGLCustomLayer, zIndexAMap, zIndex } = option;
    const layerGL = new AMap.GLCustomLayer({
      // 图层的层级
      zIndex: 118,
      // 初始化的操作，创建图层过程中执行一次。
      init: gl => {
        // 找到地图容器
        const elMapContainer = map.getContainer();
        // 找到地图图层
        const elLayerContainer = elMapContainer.querySelector(".amap-layers");
        const elMapLayer = elLayerContainer.querySelector("canvas.amap-layer");
        const qt = layerGL.qt = new QuickThree(mergeDeep({
          // 挂载元素
          el: elMapContainer,
          // dpr
          dpr: window.devicePixelRatio > 1 ? 1.5 : 1,
          // 是否启用动画帧
          enableAnimate: false,
          // 是否开启TWEEN.update
          enableUpdateTween: false,
          // 插件
          plugins: [
            PluginGui,
            PluginAnimation,
            {
              plugin: PluginRenderer,
              option: {
                enableShadow: true,
                releaseWebGLContextOnDestroy: !useGLCustomLayer,
                params: {
                  alpha: !useGLCustomLayer,
                  context: useGLCustomLayer ? gl : null,
                  canvas: useGLCustomLayer ? elMapLayer : undefined,
                  precision: "lowp"
                }
              }
            },
            PluginAdapt,
            PluginEvents,
            PluginEdit,
            PluginFns,
            PluginEffectComposer
          ]
        }, optionQt));
        qt.gl = gl;
        qt.canvasMapLayer = elMapLayer;
        if (useGLCustomLayer) {
          // 自动清空画布这里必须设置为 false，否则地图底图将无法显示
          qt.renderer.autoClear = false;
        } else {
          if (!elMapLayer.getAttribute("data-qt-marked")) {
            elMapLayer.setAttribute("data-qt-marked", "1");
            addCss(elMapLayer, {
              position: "absolute",
              left: "0",
              top: "0",
              zIndex: zIndexAMap
            });
          }
          // 插入元素位置
          const elInsertTarget = insertTarget || elMapLayer;
          qt.renderer.domElement.setAttribute("data-qt-name", option.optionQt ? option.optionQt.name : "qt");
          addCss(qt.el, {
            position: "absolute",
            left: "0",
            top: "0",
            zIndex
          });
          elInsertTarget.insertAdjacentElement(
            insertAdjacent === QuickThreeCreatorInsertAdjacent.Before ? "beforebegin" : "afterend",
            qt.el
          );
        }
        resolve(layerGL);
      },
      render() {
        const qt = layerGL.qt;
        // 自定义地图颜色
        // // 创建帧缓冲对象和纹理
        // const fbo = gl.createFramebuffer();
        // gl.bindFramebuffer(gl.FRAMEBUFFER, fbo);
        //
        // const texture = gl.createTexture();
        // gl.bindTexture(gl.TEXTURE_2D, texture);
        // gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, qt.canvasMapLayer.width, qt.canvasMapLayer.height, 0, gl.RGBA, gl.UNSIGNED_BYTE, null);
        // gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
        // gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
        //
        // const renderbuffer = gl.createRenderbuffer();
        // gl.bindRenderbuffer(gl.RENDERBUFFER, renderbuffer);
        // gl.renderbufferStorage(gl.RENDERBUFFER, gl.DEPTH_COMPONENT16, qt.canvasMapLayer.width, qt.canvasMapLayer.height);
        //
        // gl.framebufferTexture2D(gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0, gl.TEXTURE_2D, texture, 0);
        // gl.framebufferRenderbuffer(gl.FRAMEBUFFER, gl.DEPTH_ATTACHMENT, gl.RENDERBUFFER, renderbuffer);
        //
        // const vertexShaderSource = `
        //   attribute vec2 a_position;
        //   attribute vec2 a_texCoord;
        //   varying vec2 v_texCoord;
        //   void main() {
        //     gl_Position = vec4(a_position, 0.0, 1.0);
        //     v_texCoord = a_texCoord;
        //   }
        // `;
        // // 创建顶点着色器程序
        // const vertexShader = gl.createShader(gl.VERTEX_SHADER);
        // gl.shaderSource(vertexShader, vertexShaderSource);
        // gl.compileShader(vertexShader);
        //
        // const  fragmentShaderSource = `
        //   precision mediump float;
        //   uniform sampler2D u_image;
        //   varying vec2 v_texCoord;
        //   void main() {
        //     vec4 color = texture2D(u_image, v_texCoord);
        //     float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
        //     gl_FragColor = vec4(vec3(gray), color.a);
        //   }
        // `;
        // // 创建片元着色器程序
        // const fragmentShader = gl.createShader(gl.FRAGMENT_SHADER);
        // gl.shaderSource(fragmentShader, fragmentShaderSource);
        // gl.compileShader(fragmentShader);
        //
        // // 创建着色器程序
        // const program = gl.createProgram();
        // gl.attachShader(program, vertexShader);
        // gl.attachShader(program, fragmentShader);
        // gl.linkProgram(program);
        //
        // // 启用顶点属性
        // const positionAttributeLocation = gl.getAttribLocation(program, "a_position");
        // const texCoordAttributeLocation = gl.getAttribLocation(program, "a_texCoord");
        // gl.enableVertexAttribArray(positionAttributeLocation);
        // gl.enableVertexAttribArray(texCoordAttributeLocation);
        //
        // // 设置顶点数据
        // const positionBuffer = gl.createBuffer();
        // gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);
        // gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([
        //   -1, -1,
        //   1, -1,
        //   -1, 1,
        //   -1, 1,
        //   1, -1,
        //   1, 1,
        // ]), gl.STATIC_DRAW);
        // gl.vertexAttribPointer(positionAttributeLocation, 2, gl.FLOAT, false, 0, 0);
        //
        // // 设置纹理数据
        // const texCoordBuffer = gl.createBuffer();
        // gl.bindBuffer(gl.ARRAY_BUFFER, texCoordBuffer);
        // gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([
        //   0, 0,
        //   1, 0,
        //   0, 1,
        //   0, 1,
        //   1, 0,
        //   1, 1,
        // ]), gl.STATIC_DRAW);
        // gl.vertexAttribPointer(texCoordAttributeLocation, 2, gl.FLOAT, false, 0, 0);
        //
        // // 绘制场景
        // gl.useProgram(program);
        // gl.uniform1i(gl.getUniformLocation(program, "u_texture"), 0);
        // gl.activeTexture(gl.TEXTURE0);
        // gl.bindTexture(gl.TEXTURE_2D, texture);
        // gl.drawArrays(gl.TRIANGLE_STRIP, 0, 4);
        // qt融合
        if (!useGLCustomLayer) return false;
        const customCoords = map.customCoords;
        const { near, far, fov, up, lookAt, position } = customCoords.getCameraParams();
        qt.performanceMonitor && qt.performanceMonitor.begin();
        // 重新设置图层的渲染中心点，将模型等物体的渲染中心点重置
        // 否则和 LOCA 可视化等多个图层能力使用的时候会出现物体位置偏移的问题
        customCoords.setCenter(option.offsetOfLnglat);
        // 这里必须执行！！重新设置 three 的 gl 上下文状态。
        qt.renderer.resetState();
        // 这里的顺序不能颠倒，否则可能会出现绘制卡顿的效果。
        qt.camera.near = near;
        qt.camera.far = far;
        qt.camera.fov = fov;
        qt.camera.position.set(...position);
        qt.camera.up.set(...up);
        qt.camera.lookAt(...lookAt);
        qt.camera.updateProjectionMatrix();
        qt.render();
        qt.option.enableUpdateTween && update();
        // 这里必须执行！！重新设置 three 的 gl 上下文状态。
        qt.renderer.resetState();
        qt.performanceMonitor && qt.performanceMonitor.end();
      }
    });
    layerGL.option = option;
    map.addLayer(layerGL);
    // map.getLayers().forEach(layer => {
    //   console.info(layer["CLASS_NAME"], layer.getzIndex());
    //   !["AMap.GLCustomLayer", "AMap.VectorLayer", "AMap.TileLayer.Satellite"].includes(layer["CLASS_NAME"]) && layer.hide();
    //   // layer["CLASS_NAME"] === "AMap.SkyLayer" && layer.hide();
    //   // layer["CLASS_NAME"] === "AMap.NebulaLayer " && layer.hide();
    //   // layer["CLASS_NAME"] === "AMap.VectorLayer " && layer.hide();
    // });
    // setTimeout(() => !useGLCustomLayer && map.removeLayer(layerGL));
    layerGL.handleDestroy = () => {
      layerGL.qt.destroy();
      layerGL.destroy();
      useGLCustomLayer && map.removeLayer(layerGL);
    };
    // map.setStatus({ dragEnable: false, zoomEnable: false, pitchEnable: false, rotateEnable: false });
    layerGL.handleRender = delta => {
      const qt = layerGL.qt;
      if (!useGLCustomLayer && qt) {
        qt.performanceMonitor && qt.performanceMonitor.begin();
        const customCoords = map.customCoords;
        customCoords.setCenter(option.offsetOfLnglat);
        const { near, far, fov, position, up, lookAt } = customCoords.getCameraParams();
        qt.camera.near = near;
        qt.camera.far = far;
        qt.camera.fov = fov;
        qt.camera.position.set(...position);
        qt.camera.up.set(...up);
        qt.camera.lookAt(...lookAt);
        qt.camera.updateProjectionMatrix();
        qt.render(delta);
        qt.option.enableUpdateTween && update();
        qt.performanceMonitor && qt.performanceMonitor.end();
      }
    };
    // 定义地图目标点移动至世界原点的偏移经纬度
    const customCoords = map.customCoords;
    Object.defineProperty(layerGL, "offsetOfLnglat", {
      get() {
        return layerGL.option.offsetOfLnglat;
      },
      set(offsetOfLnglat) {
        layerGL.option.offsetOfLnglat = offsetOfLnglat;
        customCoords.setCenter(offsetOfLnglat);
        layerGL.offsetOfCoords = map.lngLatToCoords(layerGL.option.offsetOfLnglat);
      }
    });
    layerGL.offsetOfLnglat = option.offsetOfLnglat;
    layerGL.lngLatToCoords = customCoords.lngLatToCoord.bind(customCoords);
    layerGL.coordsToLnglat = coords => {
      const lngLat = map.coordsToLngLat(coords.map((v, i) => v + layerGL.offsetOfCoords[i]));
      return lngLat.toString().split(",").map(v => Number(v));
    };
  });
}
