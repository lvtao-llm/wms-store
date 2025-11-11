<template>
  <div class="VdQuadrilateralLineFLow position-abs">
    <p class="position-abs">
      <span class="position-abs"></span>
    </p>
    <p class="position-abs">
      <span class="position-abs"></span>
    </p>
    <p class="position-abs">
      <span class="position-abs"></span>
    </p>
    <p class="position-abs">
      <span class="position-abs"></span>
    </p>
  </div>
</template>

<script>
export default {
  name: "VdQuadrilateralLineFLow"
};
</script>

<style lang="scss" scoped>
$lineTime: 6s;
$lineWidth: 3px;
$lineHeight: 3px;
$moveCol: rgba(#00ddff, 0.75);
.position-abs {
  position: absolute;
}
.VdQuadrilateralLineFLow {
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  pointer-events: none;
  > p {
    overflow: hidden;
    span {
      width: 100%;
      height: 100%;
    }
    &:nth-child(1) {
      top: 0;
      left: 0;
      width: 100%;
      height: $lineHeight;
      span {
        top: 0;
        left: -100%;
      }
    }
    &:nth-child(2) {
      top: 0;
      right: 0;
      width: $lineWidth;
      height: 100%;
      span {
        top: -100%;
        left: 0;
      }
    }
    &:nth-child(3) {
      bottom: 0;
      right: 0;
      width: 100%;
      height: $lineHeight;
      span {
        top: 0;
        right: -100%;
      }
    }
    &:nth-child(4) {
      top: 0;
      left: 0;
      width: $lineWidth;
      height: 100%;
      span {
        bottom: -100%;
        left: 0;
      }
    }
    @keyframes move1 {
      0% {
        left: -100%;
      }
      50%, 100% {
        left: 100%;
      }
    }
    @keyframes move2 {
      0% {
        top: -100%;
      }
      50%, 100% {
        top: 100%;
      }
    }
    @keyframes move3 {
      0% {
        right: -100%;
      }
      50%, 100% {
        right: 100%;
      }
    }
    @keyframes move4 {
      0% {
        bottom: -100%;
      }
      50%, 100% {
        bottom: 100%;
      }
    }
    @for $i from 1 through 4 {
      &:nth-child(#{$i}) {
        span {
          animation: move#{$i} $lineTime infinite linear;
          animation-delay: $lineTime / 4 * ($i - 1);
          background: linear-gradient(90deg * $i, transparent, $moveCol);
        }
      }
    }
  }
}
</style>
