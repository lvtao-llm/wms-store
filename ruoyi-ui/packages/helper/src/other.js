import { trim } from "./string";
// 判断变量是否未空
function isEmpty(arg) {
  return arg === undefined || arg === null || (typeof arg === "string" ? trim(arg) === "" : false);
}
export {
  isEmpty
};