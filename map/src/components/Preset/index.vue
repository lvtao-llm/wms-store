<template>
  <div class="simple-multiplier">
    <select v-model="selected" @change="emitValue">
      <option v-for="option in options" :key="option" :value="option">
        {{ option }}x
      </option>
    </select>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  modelValue: {
    type: Number,
    default: 5,
  },
  options: {
    type: Array,
    default: () => [10, 20, 50],
  },
});

const emit = defineEmits(["update:modelValue"]);
const selected = ref(props.modelValue);

// 同步外部值
watch(
  () => props.modelValue,
  (newVal) => {
    selected.value = newVal;
  }
);

// 选择变化时触发
const emitValue = () => {
  emit("update:modelValue", selected.value);
  emit("cb", selected.value);
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
