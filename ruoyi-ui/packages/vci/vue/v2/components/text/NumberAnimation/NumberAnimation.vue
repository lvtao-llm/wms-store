<template>
  <div class="NumberAnimation">
    <template v-for="(v, i) in valueArr">
      <NumberSingle
        v-if="!isNaN(Number(v))"
        :key="i"
        :delay="i"
        :display-background="displayBackground"
        :height="heightTransformed"
        :value="v"
        :width="widthTransformed"
      />
      <i
        v-else-if="v === '.'"
        :key="i"
        class="na-point"
      >.</i>
      <i
        v-else-if="v === ','"
        :key="i"
        class="na-comma"
      >,</i>
      <span
        v-else-if="v === '-'"
        :key="i"
        :style="{ width: widthTransformed, height: heightTransformed }"
        class="na-empty vci-ta-c"
      >-</span>
    </template>
  </div>
</template>

<script>
import NumberSingle from "./NumberSingle.vue";
import { isEmpty } from "@vci/helper/src/other";
import { toVw } from "../../../../../other/adapt";

export default {
  name: "NumberAnimation",
  components: { NumberSingle },
  props: {
    // 展示的数据
    value: {
      type: [Number, String],
      default: null
    },
    // 小数点位数
    point: {
      type: [Number, String],
      default: 0
    },
    // 是否每三位分割逗号
    spThree: {
      default: false,
      type: Boolean
    },
    // 单个数字尺寸 宽  均在项目规定尺寸进行转换
    width: {
      default: 20,
      type: [String, Number]
    },
    // 单个数字尺寸 高
    height: {
      default: 24,
      type: [String, Number]
    },
    // 是否显示背景
    displayBackground: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      valueArr: []
    };
  },
  computed: {
    widthTransformed() {
      return toVw(this.width);
    },
    heightTransformed() {
      return toVw(this.height);
    }
  },
  watch: {
    value: {
      handler: "analysis",
      immediate: true
    }
  },
  methods: {
    analysis(value) {
      if (isEmpty(value)) {
        this.valueArr = ["-"];
      } else {
        this._value = Number(value);
        if (isNaN(this._value)) {
          console.warn("请传入一个数字!");
        } else {
          if (this._value === 0) {
            this.valueArr = [0];
          } else {
            const preZeroArray = (value + "").split("").reduce(
              (pool, v) => {
                v !== "0" && (pool.fixed = true);
                !pool.fixed && v === "0" && pool.push(v);
                return pool;
              },
              []
            );
            this._value = this._value.toFixed(Number(this.point));
            if (this.spThree) {
              const it = (this._value + "").split(".")[0];
              const ft = (this._value + "").split(".")[1];
              const ni = it.split("").reverse().map((v, idx) => idx % 3 === 2 ? `${v},` : v).join("").split("").reverse().concat(ft ? ("." + ft).split("") : []);
              if (ni[0] === ",") ni.shift();
              this.valueArr = ni;
            } else {
              this.valueArr = preZeroArray.concat((this._value + "").split(""));
            }
          }
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.NumberAnimation {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-wrap: wrap;
  .NumberSingle, .na-point, .na-comma, .na-empty {
    flex-shrink: 0;
  }
}
</style>
