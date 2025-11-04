import request from '@/utils/request'

// 查询车辆黑名单wms_vehicle_blacklist列表
export function listWms_vehicle_record(query) {
  return request({
    url: '/system/wms_vehicle_record/list',
    method: 'get',
    params: query
  })
}

// 查询车辆黑名单wms_vehicle_blacklist详细
export function getWms_vehicle_record(id) {
  return request({
    url: '/system/wms_vehicle_record/' + id,
    method: 'get'
  })
}

// 新增车辆黑名单wms_vehicle_blacklist
export function addWms_vehicle_record(data) {
  return request({
    url: '/system/wms_vehicle_record',
    method: 'post',
    data: data
  })
}

// 修改车辆黑名单wms_vehicle_blacklist
export function updateWms_vehicle_record(data) {
  return request({
    url: '/system/wms_vehicle_record',
    method: 'put',
    data: data
  })
}

// 删除车辆黑名单wms_vehicle_blacklist
export function delWms_vehicle_record(id) {
  return request({
    url: '/system/wms_vehicle_record/' + id,
    method: 'delete'
  })
}
