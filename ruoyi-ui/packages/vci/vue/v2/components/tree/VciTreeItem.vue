<template>
  <div class="VciTreeItem">
    <div
      :style="{ cursor: enableSelectionItem ? 'pointer' : 'default' }"
      class="vti vci-flex-rml vci-pst-rlv"
      @click="enableSelectionItem && clickCheckBox(item)"
    >
      <div
        v-if="item.children && item.children.length > 0"
        :class="{'vu-folding': item.folding}"
        class="vti-uf vci-flex-rcm vci-flex-0-0 vci-pst-m"
        @click.stop="clickFold(item)"
      >
        <slot
          name="item-uf"
          v-bind="item"
        >
          <i class="vu vci-fs-12">▼</i>
        </slot>
      </div>
      <div
        v-if="enableSelection"
        :class="{'vc-checked': item.checked}"
        class="vti-checkbox vci-flex-0-0"
        @click.stop="clickCheckBox(item)"
      >
        <slot
          name="item-checkbox"
          v-bind="item"
        >
          <i class="vc vci-after vci-flex-rcm"></i>
        </slot>
      </div>
      <div class="vti-name vci-flex-rml">
        <slot
          name="item-name"
          v-bind="item"
        >
          <span>{{ item.name }}</span>
        </slot>
      </div>
      <VciSpace />
      <slot
        name="item-other"
        v-bind="item"
      ></slot>
    </div>
    <slot
      v-if="!item.folding"
      name="children"
      v-bind="{ item, children: item.children }"
    ></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2024/9/13 11:15
import VciSpace from "../other/VciSpace.vue";

export default {
  name: "VciTreeItem",
  components: { VciSpace },
  props: {
    item: {
      type: Object,
      default: null
    },
    parent: {
      type: Object,
      default: null
    },
    enableSelection: {
      type: Boolean,
      default: false
    },
    enableSelectionItem: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {};
  },
  methods: {
    clickCheckBox(item) {
      item.checked = !item.checked;
      this.$emit("change-checked", { checked: item.checked, item });
    },
    clickFold(item) {
      item.folding = !item.folding;
    }
  }
};
</script>

<style lang="scss" scoped>
.VciTreeItem {
  .vti {
    .vti-uf {
      right: 100%;
      cursor: pointer;
      transition: .12s ease-in-out;
      .vu {
        padding-right: 4px;
      }
      &.vu-folding {
        transform: translateY(-50%) rotateX(180deg);
      }
    }
    .vti-checkbox {
      cursor: pointer;
      .vc {
        width: 12px;
        height: 12px;
        border: 1px solid #fff;
        border-radius: 2px;
        &::after {
          width: 6px;
          height: 6px;
          background-color: #fff;
          transition: .12s ease-in-out;
          opacity: 0;
          transform: scale(0);
        }
      }
      &.vc-checked .vc::after {
        opacity: 1;
        transform: scale(1);
      }
    }
    .vti-name {
      padding: 0 4px;
    }
  }
  .VciTreeChildren {
    padding-left: 18px;
  }
}
</style>