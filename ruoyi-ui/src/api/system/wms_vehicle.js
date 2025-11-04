import request from '@/utils/request'

// 查询车辆档案列表
export function listWms_vehicle(query) {
  return request({
    url: '/system/wms_vehicle/list',
    method: 'get',
    params: query
  })
}

// 查询车辆档案详细
export function getWms_vehicle(vehicleId) {
  return request({
    url: '/system/wms_vehicle/' + vehicleId,
    method: 'get'
  })
}

// 新增车辆档案
export function addWms_vehicle(data) {
  return request({
    url: '/system/wms_vehicle',
    method: 'post',
    data: data
  })
}

// 修改车辆档案
export function updateWms_vehicle(data) {
  return request({
    url: '/system/wms_vehicle',
    method: 'put',
    data: data
  })
}

// 删除车辆档案
export function delWms_vehicle(vehicleId) {
  return request({
    url: '/system/wms_vehicle/' + vehicleId,
    method: 'delete'
  })
}
