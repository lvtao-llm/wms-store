import request from '@/utils/request'

// 查询车辆黑名单列表
export function listWms_vehicle_blacklist(query) {
  return request({
    url: '/system/wms_vehicle_blacklist/list',
    method: 'get',
    params: query
  })
}

// 查询车辆黑名单详细
export function getWms_vehicle_blacklist(id) {
  return request({
    url: '/system/wms_vehicle_blacklist/' + id,
    method: 'get'
  })
}

// 新增车辆黑名单
export function addWms_vehicle_blacklist(data) {
  return request({
    url: '/system/wms_vehicle_blacklist',
    method: 'post',
    data: data
  })
}

// 修改车辆黑名单
export function updateWms_vehicle_blacklist(data) {
  return request({
    url: '/system/wms_vehicle_blacklist',
    method: 'put',
    data: data
  })
}

// 删除车辆黑名单
export function delWms_vehicle_blacklist(id) {
  return request({
    url: '/system/wms_vehicle_blacklist/' + id,
    method: 'delete'
  })
}
