<template>
  <div class="navbar">
    <hamburger
      id="hamburger-container"
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />

    <breadcrumb
      v-if="!topNav"
      id="breadcrumb-container"
      class="breadcrumb-container"
    />
    <top-nav v-if="topNav" id="topmenu-container" class="topmenu-container" />

    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <!-- <search id="header-search" class="right-menu-item" /> -->
        <el-tooltip content="跳转首页大屏" effect="dark" placement="bottom">
          <img
            @click="to3d"
            style="width: 23px; height: 23px"
            class="icon-img"
            src="@/assets/images/地图.svg"
          />
        </el-tooltip>
        <el-tooltip content="车辆报警" effect="dark" placement="bottom">
          <img
            style="width: 23px; height: 23px"
            class="icon-img"
            src="@/assets/images/车辆报警.svg"
          />
        </el-tooltip>
        <el-tooltip content="人员报警" effect="dark" placement="bottom">
          <img
            @click="peopleClick"
            v-if="peopleWarning"
            fill="red"
            class="icon-img blinking-red"
            src="@/assets/images/人员报警-red.svg"
          />
          <img
            @click="peopleClick"
            v-else
            fill="red"
            class="icon-img blinking"
            src="@/assets/images/人员报警.svg"
          />
        </el-tooltip>
        <el-tooltip content="物料报警" effect="dark" placement="bottom">
          <img class="icon-img" src="@/assets/images/报警.svg" />
        </el-tooltip>
        <img
          @click="radioClose"
          v-if="radioOn"
          class="icon-img"
          src="@/assets/images/喇叭开.svg"
        />
        <img
          @click="radioOpen"
          v-if="!radioOn"
          class="icon-img"
          src="@/assets/images/喇叭关.svg"
        />
        <screenfull id="screenfull" class="right-menu-item hover-effect" />
      </template>

      <el-dropdown
        class="avatar-container right-menu-item hover-effect"
        trigger="hover"
      >
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar" />
          <span class="user-nickname"> {{ nickName }} </span>
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setLayout" v-if="setting">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <audio style="display: none" controls ref="audio" class="aud">
      <source src="./childView/警报.mp3" />
    </audio>
    <peopleWarningDialog ref="people"></peopleWarningDialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import TopNav from "@/components/TopNav";
import Hamburger from "@/components/Hamburger";
import Screenfull from "@/components/Screenfull";
import SizeSelect from "@/components/SizeSelect";
import Search from "@/components/HeaderSearch";
import RuoYiGit from "@/components/RuoYi/Git";
import RuoYiDoc from "@/components/RuoYi/Doc";
import peopleWarningDialog from "./childView/peopleWarningDialog.vue";

export default {
  emits: ["setLayout"],
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc,
    peopleWarningDialog,
  },
  data() {
    return {
      peopleWarning: false,
      radioOn: false,
    };
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "device", "nickName"]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings;
      },
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav;
      },
    },
  },
  methods: {
    peopleClick() {
      // this.peopleWarning = !this.peopleWarning;
      this.$refs.people.openDia();
    },
    radioOpen() {
      this.radioOn = true;
      this.$refs.audio.play();
    },
    radioClose() {
      this.radioOn = false;
      this.$refs.audio.pause();
    },
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
    },
    setLayout(event) {
      this.$emit("setLayout");
    },
    to3d() {
      const baseUrl = process.env.VUE_APP_BASE_URL;
      const index = baseUrl.lastIndexOf(":");
      const str = baseUrl.substring(0, index);
      const type = str.slice(-2);
      const root = type == "93" ? ":10032" : ":8095";
      const url = str + root;
      console.log();
      window.open(url, "_self");
    },
    logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$store.dispatch("LogOut").then(() => {
            location.href = "/index";
          });
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.blinking-red {
  animation: slowBlinkRed 2s infinite; /* 2秒一次，速度较慢 */
}

@keyframes slowBlinkRed {
  0%,
  100% {
    opacity: 1; /* 正常状态，完全不透明 */
    filter: none; /* 不应用任何滤镜，原色 */
  }
  50% {
    opacity: 0.3; /* 闪烁时变暗，增强对比 */
    // filter: brightness(1.3) saturate(4) hue-rotate(340deg);
  }
}
.icon-img {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;
  width: 20px;
  height: 20px;
  vertical-align: 13px;
  margin-right: 5px;
}
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 0px;
      padding-right: 0px;

      .avatar-wrapper {
        margin-top: 10px;
        right: 8px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 30px;
          height: 30px;
          border-radius: 50%;
        }

        .user-nickname {
          position: relative;
          bottom: 10px;
          left: 2px;
          font-size: 14px;
          font-weight: bold;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
