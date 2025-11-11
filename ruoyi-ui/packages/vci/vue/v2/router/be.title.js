import { setTitle } from "@vci/helper/src/document";
import { isEmpty } from "@vci/helper/src/other";

/**
 * 路由导航前处理     标题设置
 * @param router    路由实例
 * @param name      项目名称
 */
export const beforeEachForTitle = (router, name) => {
  if (!name) {
    console.warn("请在主程序路由配置文件中引入package.json\n将其中的name-cn赋给`name`!");
    console.warn("如果package.json中没有name-cne这个属性,请自动添加: \n他的作用是用来显示页面的标题!");
  }
  router.beforeEach((to, from, next) => {
    if (name && to.meta && !isEmpty(to.meta["title"])) setTitle(`${name} - ${to.meta["title"]}`);
    else setTitle(name);
    next();
  });
};
