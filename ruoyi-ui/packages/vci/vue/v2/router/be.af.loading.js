import { PluginLoading } from "../components/other/VpLoading";

/**
 * 路由导航前后处理    路由载入等待
 * @param router    路由实例
 */
export const beforeAfterEachForLoading = router => {
  router.beforeEach((to, from, next) => {
    clearTimeout(beforeAfterEachForLoading.inter);
    PluginLoading.open({ text: "载入中" });
    next();
  });
  router.afterEach(() => {
    clearTimeout(beforeAfterEachForLoading.inter);
    beforeAfterEachForLoading.inter = setTimeout(() => PluginLoading.close());
  });
};
