import VciScroll from "../other/VciScroll.vue";
import { toVh } from "../../../../other/adapt";
import VciCell from "./VciCell.vue";
import VciEmpty from "../other/VciEmpty.vue";
import { isFoundationType } from "@vci/helper/src/object";

function getValueFromObjectByProp(object, prop) {
  const propArray = (prop || "").split(".");
  if (isFoundationType(object)) return null;
  else {
    const p1 = object[propArray[0]];
    if (propArray.length <= 1) return p1;
    else {
      let targetObject = p1;
      for (let i = 1; i < propArray.length; i++) {
        if (isFoundationType(targetObject)) {
          targetObject = "undefined";
          break;
        }
        targetObject = targetObject[propArray[i]];
      }
      return targetObject;
    }
  }
}

export default {
  functional: true,
  name: "VciTable",
  props: {
    list: {
      type: Array,
      default: null
    },
    heightRow: {
      type: Number,
      default: 28
    },
    heightRowHeader: {
      type: Number,
      default: null
    },
    scrollTheme: {
      type: String,
      default: "#fff"
    },
    scrollThemeEnd: {
      type: String,
      default: "#fff"
    }
  },
  render(h, context) {
    const columns = (context.children || []).filter(vn => vn.data.isVciColumn);
    const list = context.props.list;
    const isEmpty = !(list && list.length);
    const heightHeader = toVh(context.props.heightRowHeader || context.props.heightRow);
    return h(
      "div",
      {
        class: ["VciTable vci-h-100", context.data.staticClass]
      },
      [
        h(
          "div",
          {
            class: "vt-header",
            style: {
              height: heightHeader
            }
          },
          [
            h(
              VciScroll,
              {
                props: {
                  visibleHorizontalBar: false
                },
                ref: "headerScroll"
              },
              [
                h(
                  "div",
                  {
                    class: "vt-tr vci-flex-rml",
                    style: {
                      height: heightHeader
                    }
                  },
                  columns.map(vn => {
                    const props = vn.data.props;
                    return h(
                      VciCell,
                      {
                        props: {
                          ...props,
                          isHeader: true,
                          value: props.name
                        }
                      }
                    );
                  })
                )
              ]
            )
          ]
        ),
        h(
          "div",
          {
            class: "vt-body",
            style: { height: `calc(100% - ${heightHeader})` }
          },
          [
            h(
              VciScroll,
              {
                props: {
                  theme: context.props.scrollTheme,
                  themeEnd: context.props.scrollThemeEnd
                },
                on: {
                  scroll(e) {
                    context.parent.$refs.headerScroll.scrollLeft(e.left);
                  }
                }
              },
              isEmpty ?
                [
                  h(
                    VciEmpty,
                    {
                      class: "vci-flex-rcm",
                      style: {
                        paddingTop: "4vh"
                      },
                      props: { text: "暂无数据" }
                    }
                  )
                ]
                : list.map((item, i) => h(
                  "div",
                  {
                    class: ["vt-tr vci-flex-rml", i % 2 === 0 ? "vtr-even" : "vtr-odd"]
                  },
                  columns.map((vn, iv) => {
                    const props = vn.data.props;
                    return h(
                      VciCell,
                      {
                        props: {
                          ...props,
                          height: props.height ? props.height : context.props.heightRow,
                          isLastColumn: iv === columns.length - 1,
                          value: props.isOrderNumber ? (i + 1) : getValueFromObjectByProp(item, props.prop),
                          item
                        },
                        scopedSlots: vn.data.scopedSlots
                      }
                    );
                  })
                ))
            )
          ]
        )
      ]
    );
  }
};