<template>
  <div>
    <iframe
      style="width: 100%; height: 85vh"
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
      // imgSrc: "http://112.98.110.101:8093",
      imgSrc: defaultSettings.iframeUrl + "?type=行动轨迹",
    };
  },
};
</script>
