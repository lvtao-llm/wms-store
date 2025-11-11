import { Vf } from "../../../helper/v-fetch";

/**
 * 路由导航前处理 - 中断所有未完成请求
 * @param router    路由实例
 */
export const beforeEachForRequest = router => {
  router.beforeEach((to, from, next) => {
    // 跳转路由前取消所有请求
    Vf.abortAllVfs();
    next();
  });
};
