<template>
  <div class="TheHeader vci-pst-rlv font-pm-zd">
    <div class="title vci-pst-cm">仓库3D数字孪生系统</div>
    <div class="date-frame vci-flex-rml">
      <div class="year-date vci-flex-rml">
        <div class="icon vci-bg-fill"></div>
        <div class="year font-linear-color">
          {{ datetime.format("YYYY.MM.DD") }}
        </div>
        <div class="date font-linear-color">
          {{ datetime.format("HH:mm:ss") }}
        </div>
      </div>
      <div class="week vci-flex-rml">
        <div class="icon vci-bg-fill"></div>
        <div class="wk font-linear-color">
          {{ datetime.format("dddd") }}
        </div>
      </div>
      <div class="vci-flex-rml">
        <div class="font-linear-color lunar-date1">
          {{ lunar }}
        </div>
      </div>
      <div @click="goBack" class="goBackCss">返回后台</div>
    </div>
  </div>
</template>

<script>
import MixinDateTime from "@vci/vci/vue/v2/mixins/MixinDateTime";
import { Solar } from "lunar-javascript";
import MixinRefresh from "@vci/vci/vue/v2/mixins/MixinRefresh";

export default {
  name: "TheHeader",
  mixins: [MixinDateTime, MixinRefresh],
  data() {
    return {
      lunar: "",
    };
  },
  created() {
    this.refresh(this.getLunarDate, 1e3);
  },
  methods: {
    getLunarDate() {
      const solar = Solar.fromDate(new Date());
      const lunar = solar.getLunar();
      this.lunar = "农历" + lunar.toString().split("年")[1];
    },
    goBack() {
      window.open("http://112.98.110.101:8092", "_self");
    },
  },
};
</script>

<style lang="scss" scoped>
// @import "@vci/style/src/mixins/font";
@import "@vci/style";
.TheHeader {
  // height: 5vh;
  .goBackCss {
    margin-left: 50vw;
    // margin-top: 3vh;
    pointer-events: auto;
    cursor: pointer;
    font-size: 15px !important;
  }

  .title {
    // top: 60%;
    font-size: 3rem;
    letter-spacing: 12px;
    text-shadow: 0 0 13px #0068de;
  }
  .date-frame {
    left: 1vw;
    bottom: 0;
    font-size: 1rem;
    .font-linear-color {
      // @include vci-font-linear-gradient(to bottom, #fff, (#f5f9fc, #82b0df));
    }
    .year-date {
      font-size: 1rem;
      .icon {
        width: 20px;
        height: 20px;
        background-image: url("./img/icon.date.png");
      }
      letter-spacing: 1px;
      .year {
        margin-left: 0.5vw;
      }
      .date {
        width: 5vw;
        margin-left: 0.1vw;
      }
    }
    .week {
      font-size: 1rem;
      margin-left: 0.1vw;
      margin-right: 0.1vw;

      .icon {
        width: 1vw;
        height: 1vh;
        margin-right: 0.5vw;
        background-image: url("./img/icon.calendar.png");
      }
      .wk {
        width: 5vw;
      }
    }
    .lunar-date1 {
      font-size: 1rem;
      width: 8vw !important;
    }
  }
}
</style>
