<template>
  <i class="VciClose" @click="$emit('click', $event)"></i>
</template>

<script>
// power by visual-ceiling, at 2022/7/14 13:47
export default {
  name: "VciClose",
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
.VciClose {
  position: relative;
  cursor: pointer;
  display: block;
  width: 16px;
  height: 16px;
  &::after,
  &::before {
    position: absolute;
    left: 50%;
    top: 50%;
    content: " ";
    display: block;
    width: 100%;
    height: 3px;
    border-radius: 3px;
    background-color: #fff;
    transform: translate(-50%, -50%);
    transition: 0.12s;
  }
  &::before {
    transform: translate(-50%, -50%) rotateZ(45deg);
  }
  &::after {
    transform: translate(-50%, -50%) rotateZ(-45deg);
  }
  &:hover {
    &::before,
    &::after {
      animation: amtLwClose 0.35s 1 both ease-in-out;
    }
    &::after {
      animation-delay: 0.1s;
    }
    @keyframes amtLwClose {
      from {
        width: 0;
      }
      to {
        width: 100%;
      }
    }
  }
  &:active {
    transform: scale(0.8);
  }
}
</style>
