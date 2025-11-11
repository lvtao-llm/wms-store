/**
 * 检索目标路由子路由
 * @param routeName     目标路由name
 * @param $router       路由实例
 * @returns {*[]}
 */
function findRouteChildren(routeName, $router) {
  function fn(routes) {
    let target = [];
    for (let i = 0; i < routes.length; i++) {
      const route = routes[i];
      if (route.name === routeName) {
        target = route.children;
        break;
      } else if (route.children) target = fn(route.children);
    }
    return target;
  }
  return fn($router.options.routes);
}

/**
 * 获取目标路由全路径
 * @param route   目标路由
 * @param $router 路由实例
 */
function getRouteFullPath(route, $router) {
  let targetPath;
  function fn(routes, path = "") {
    for (let i = 0; i < routes.length; i++) {
      const currentRoute = routes[i];
      const currentPath = currentRoute.path.includes("/") ? currentRoute.path : `/${currentRoute.path}`;
      if (route.name === currentRoute.name) {
        targetPath = path + currentPath;
        break;
      }
      if (currentRoute.children) fn(currentRoute.children, path + currentPath);
    }
    return path;
  }
  fn($router.options.routes);
  return targetPath;
}

// 延迟
function delay(time = 1e3) {
  return new Promise(resolve => setTimeout(resolve, time));
}

export {
  findRouteChildren,
  getRouteFullPath,
  delay
};