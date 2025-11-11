<template>
  <div class="VciWeather vci-flex-rml">
    <i
      v-if="temperatureIcon"
      :class="`bi-${temperatureIcon}`"
      class="vw-icon vci-bg-contain"
    ></i>
    <div class="vw-info vci-flex-cm">
      <div
        v-if="visibleTemperature"
        class="bi-temp vci-flex-rml"
      >
        <strong>{{ temperature|eptNum }}</strong>
        <span>℃</span>
      </div>
      <span
        v-if="visibleDesc"
        class="bi-temp-desc"
      >{{ temperatureDesc }}</span>
    </div>
    <slot></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2022/8/8 16:55
import { vf } from "../../../../helper/v-fetch";

export default {
  name: "VciWeather",
  props: {
    // 数据格式 经度,纬度
    lngLat: {
      type: String,
      default: null
    },
    enableRefresh: {
      type: Boolean,
      default: false
    },
    visibleTemperature: {
      type: Boolean,
      default: false
    },
    visibleDesc: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      temperature: null,
      temperatureDesc: null,
      temperatureIcon: null,
      inter: -1
    };
  },
  watch: {
    lngLat: {
      immediate: true,
      handler(lngLat) {
        if (!lngLat) {
          this.temperature = null;
          this.temperatureDesc = null;
          this.temperatureIcon = null;
        } else {
          const fn = () => this.getWeather(lngLat).then(weather => {
            if (weather.code === "200") {
              this.temperature = weather.now.temp;
              this.temperatureDesc = weather.now.text;
              this.temperatureIcon = (() => {
                const code = Number(weather.now.icon);
                if (code === 100) return "sunny";
                else if (code <= 153) return "cloudy";
                else if (code <= 154) return "overcast";
                else return "rain";
              })();
            } else this.temperatureDesc = "次数超限";
          });
          fn();
          if (this.enableRefresh) this.inter = setInterval(fn.bind(this), 60 * 60 * 1e3);
        }
      }
    }
  },
  methods: {
    /**
     * 和风天气-实时天气
     * 详情见: https://dev.qweather.com/docs/api/weather/weather-now/
     * @param location  经纬度
     * @param key       用户key
     * @returns {Promise}
     */
    getWeather(location, key = "ee5efe84f7664cb9b5c8b93abb8b9fea") {
      return vf({
        url: `https://devapi.qweather.com/v7/weather/now?location=${location}&key=${key}`,
        withRootApi: false,
        enableResolveData: false,
        enableGlobalError: false
      }).fetch;
    }
  }
};
</script>

<style lang="scss" scoped>
.VciWeather {
  .vw-icon {
    display: block;
    width: 64px;
    height: 52px;
    margin-right: 6px;
    $weathers: "cloudy",
    "overcast",
    "rain",
    "sunny";
    @for $i from 1 to length($weathers) + 1 {
      $weather: nth($weathers, $i);
      &.bi-#{$weather} {
        background-image: url("img/#{$weather}.png");
      }
    }
  }
  .vw-info {
    .bi-temp {
      align-items: flex-end;
      strong {
        padding-right: 4px;
      }
    }
  }
}
</style>