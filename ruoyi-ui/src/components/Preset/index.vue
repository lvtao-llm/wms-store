<template>
  <div class="simple-multiplier">
    <select v-model="selected" @change="emitValue">
      <option v-for="option in options" :key="option" :value="option">
        {{ option }}x
      </option>
    </select>
  </div>
</template>

<script>
export default {
  name: "SimpleMultiplier",
  props: {
    modelValue: {
      type: Number,
      default: 5,
    },
    options: {
      type: Array,
      default: () => [10, 20, 50],
    },
  },
  data() {
    return {
      selected: this.modelValue, // 初始化选中值为传进来的 modelValue
    };
  },
  watch: {
    // 监听父组件传过来的 modelValue 变化，同步到本地的 selected
    modelValue(newValue) {
      this.selected = newValue;
    },
  },
  methods: {
    emitValue() {
      // 触发 v-model 所需要的 update:modelValue 事件
      this.$emit("update:modelValue", this.selected);
      // 同时也触发自定义的 cb 事件，传递当前选中的值
      this.$emit("cb", this.selected);
    },
  },
};
</script>

<style scoped>
.simple-multiplier select {
  border: 1px solid #ddd;
  border-radius: 4px;
  background: black;
  font-size: 14px;
  cursor: pointer;
  outline: none;
  color: #fff;
}

.simple-multiplier select:hover {
  border-color: #aaa;
}

.simple-multiplier select:focus {
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.1);
}
</style>
