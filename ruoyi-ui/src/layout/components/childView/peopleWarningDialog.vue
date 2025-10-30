<template>
  <div>
    <el-dialog
      :visible.sync="isShow"
      title="人员报警"
      width="800"
      append-to-body
    >
      111
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isShow: false,
    };
  },
  methods: {
    openDia() {
      this.isShow = true;
    },
    close() {
      this.isShow = false;
    },
  },
};
</script>
