import request from '@/utils/request'

// 查询车辆路线规划列表
export function listRoute(query) {
  return request({
    url: '/system/route/list',
    method: 'get',
    params: query
  })
}

// 查询车辆路线规划详细
export function getRoute(routeId) {
  return request({
    url: '/system/route/' + routeId,
    method: 'get'
  })
}

// 新增车辆路线规划
export function addRoute(data) {
  return request({
    url: '/system/route',
    method: 'post',
    data: data
  })
}

// 修改车辆路线规划
export function updateRoute(data) {
  return request({
    url: '/system/route',
    method: 'put',
    data: data
  })
}

// 删除车辆路线规划
export function delRoute(routeId) {
  return request({
    url: '/system/route/' + routeId,
    method: 'delete'
  })
}
