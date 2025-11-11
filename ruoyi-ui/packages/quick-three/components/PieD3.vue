<template>
  <div
    class="PieD3"
    @mouseleave="enableAutoplay && play()"
    @mousemove="pause"
  >
    <VciD3
      :before-init-qt="beforeInitQt"
      @ready="ready"
    />
    <slot></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2024/7/4 上午11:45

import VciD3 from "./VciD3.vue";
import { mergeDeep } from "@vci/helper/src/object";
import { PluginGui } from "./../src/plugins/PluginGui";
import { PluginRenderer } from "./../src/plugins/PluginRenderer";
import { PluginAdapt } from "./../src/plugins/PluginAdapt";
import { PluginEvents } from "./../src/plugins/PluginEvents";
import { PluginAnimation } from "./../src/plugins/PluginAnimation";
import { ExtrudeGeometry, Mesh, MeshStandardMaterial, Path, Shape } from "three";
import { createDirectionLight } from "./../src/creator/lights/createDirectionLight";
import { degToRad } from "three/src/math/MathUtils";
import Thing from "./../src/core/Thing";
import { sumArray } from "@vci/helper/src/array";
import { createAmbientLight } from "./../src/creator/lights/createAmbientLight";
import { createOrthographicCamera } from "./../src/creator/cameras/createOrthographicCamera";
import { PluginFns } from "./../src/plugins/PluginFns";
import { ThingEvents } from "./../src/events/ThingEvents";
import { remove, Tween } from "@tweenjs/tween.js";

const PI2 = Math.PI * 2;
export default {
  name: "PieD3",
  components: { VciD3 },
  props: {
    dataPie: {
      type: Array,
      default: null
    },
    colors: {
      type: Array,
      default: () => ["#ffa861", "#4294ff", "#34f36a", "#fd3a84"]
    },
    enableAutoplay: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      isReady: false,
      autoplay: {
        index: 0,
        inter: 0
      }
    };
  },
  watch: {
    dataPie: {
      immediate: true,
      handler() {
        this.isReady && this.update();
      }
    }
  },
  beforeDestroy() {
    this.pause();
  },
  methods: {
    beforeInitQt(option) {
      mergeDeep(option, {
        name: "Pie3D",
        debug: false,
        plugins: [
          PluginGui,
          PluginAnimation,
          {
            plugin: PluginRenderer,
            option: {
              enableShadow: true,
              params: {
                antialias: true,
                depth: true
              }
            }
          },
          PluginAdapt,
          PluginEvents,
          PluginFns
        ]
      });
    },
    async ready(qt) {
      this.qt = qt;
      qt.camera = createOrthographicCamera({
        qt,
        left: -qt.elWidth / 2,
        right: qt.elWidth / 2,
        top: qt.elHeight / 2,
        bottom: -qt.elHeight / 2
      });
      qt.camera.position.set(0, 40, 90);
      qt.camera.lookAt(0, 0, 0);
      qt.camera.updateProjectionMatrix();
      // await createEnvHDR(qt, `${resourceRootAliyun}/textures/env-light.hdr`);
      // qt.scene.background = null;
      createAmbientLight({ qt, intensity: 2 });
      createDirectionLight({ qt, intensity: 6, position: [0, 100, 0] });
      this.isReady = true;
      this.update();
      qt.animation.animationIn();
    },
    createPieItem(angle = Math.PI * .25, radius = [28, 50]) {
      const { qt } = this;
      const shape = new Shape();
      if (Array.isArray(radius)) {
        const pathInner = new Path();
        pathInner.arc(0, 0, radius[0], 0, angle);
        const pathOuter = new Path();
        pathOuter.arc(0, 0, radius[1], 0, angle);
        shape.setFromPoints(pathOuter.getPoints(99).concat(pathInner.getPoints(99).reverse()));
      } else {
        shape.moveTo(0, 0);
        shape.absarc(0, 0, radius, 0, angle);
        shape.lineTo(0, 0);
      }
      const geo = new ExtrudeGeometry(shape, {
        depth: 14,
        steps: 20,
        bevelEnabled: false
      });
      geo.rotateX(degToRad(-90));
      geo.computeVertexNormals();
      const mtl = new MeshStandardMaterial({
        roughness: .8,
        metalness: .2
      });
      const o3 = new Mesh(geo, mtl);
      const thing = new Thing({
        qt,
        object: o3
      });
      thing.isFocus = false;
      thing.focus = async () => {
        thing.isFocus = true;
        qt.things.forEach(t => t !== thing && t.blur());
        this.autoplay.index = qt.things.indexOf(thing);
        this.$emit("focus", {
          index: this.autoplay.index,
          data: this.dataPie[this.autoplay.index]
        });
        await thing.grow(2);
      };
      thing.blur = async () => {
        thing.isFocus = false;
        await thing.grow(1);
      };
      thing.grow = async (grow, duration = 100, delay = 0, immediate = false) => {
        thing.tw.grow && remove(thing.tw.grow);
        if (immediate) thing.scale = [1, grow, 1];
        else await new Promise(resolve => thing.tw.grow = new Tween(thing.scale)
          .to([1, grow, 1], duration)
          .delay(delay)
          .onUpdate(e => thing.scale = e)
          .onComplete(resolve)
          .start());
      };
      thing.addEventListener(ThingEvents.Click, () => {
        if (thing.isFocus) {
          thing.blur();
          this.$emit("blur");
        } else thing.focus();
      });
      return thing;
    },
    async update() {
      const { colors, dataPie, qt } = this;
      qt.things.length > 0 && await new Promise(resolve => qt.things.forEach(thing => thing.fadeOut({ duration: 200 }).then(thing => {
        thing.destroy();
        resolve();
      })));
      if (dataPie && dataPie.length > 0) {
        const amount = sumArray(dataPie, "value");
        this.markAngle = degToRad(45);
        dataPie.forEach((v, i) => {
          const percent = v.value / amount;
          const angle = percent * PI2;
          const thing = this.createPieItem(angle);
          thing.object.material.color.set(colors[i % colors.length]);
          thing.rotation = [0, this.markAngle, 0];
          thing.hide().fadeIn();
          thing.grow(0, 0, 0, true);
          thing.grow(1, 350, i * 175);
          this.markAngle += angle;
        });
      }
      this.enableAutoplay ? this.play() : this.pause();
    },
    play() {
      clearTimeout(this.autoplay.inter);
      this.autoplay.index++;
      if (this.autoplay.index >= this.dataPie.length) this.autoplay.index = 0;
      this.qt.things[this.autoplay.index].focus();
      this.autoplay.inter = setTimeout(() => this.play(), 2e3);
    },
    pause() {
      clearTimeout(this.autoplay.inter);
    }
  }
};
</script>

<style lang="scss" scoped>
.PieD3 {
  width: 100%;
  height: 100%;
}
</style>