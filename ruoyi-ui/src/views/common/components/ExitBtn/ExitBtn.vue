<template>
  <div class="ExitBtn vci-bg-fill vci-ta-c vci-pst-abs">
    {{ text }}
  </div>
</template>

<script>
export default {
  name: "ExitBtn",
  props: {
    text: {
      type: String,
      default: "退出历史轨迹"
    }
  }
};
</script>


<style lang="scss" scoped>
.ExitBtn {
  top: 876px;
  right: 1020px;
  width: 139px;
  height: 56px;
  background-image: url("./img/bg.1.png");
  transition: background-image .5s, text-shadow .3s;
  line-height: 56px;
  pointer-events: auto;
  cursor: pointer;
  &:hover {
    background-image: url("./img/bg.2.png");
    text-shadow: 0 0 10px #0ba6ff;
  }
}
</style>
