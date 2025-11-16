<template>
  <div>
    <iframe
      :key="indexKey"
      style="width: 100%; height: 100vh"
      id="contentFrame"
      :src="imgSrc"
    ></iframe>
  </div>
</template>
<script>
import defaultSettings from "@/settings";
export default {
  data() {
    return {
      show: false,
      indexKey: 0,
      imgSrc: defaultSettings.iframeUrl,
    };
  },
  methods: {},
};
</script>
