import request from '@/utils/request'

// 查询车辆档案列表
export function listVehicle(query) {
  return request({
    url: '/system/vehicle/list',
    method: 'get',
    params: query
  })
}

// 查询车辆档案详细
export function getVehicle(vehicleId) {
  return request({
    url: '/system/vehicle/' + vehicleId,
    method: 'get'
  })
}

// 新增车辆档案
export function addVehicle(data) {
  return request({
    url: '/system/vehicle',
    method: 'post',
    data: data
  })
}

// 修改车辆档案
export function updateVehicle(data) {
  return request({
    url: '/system/vehicle',
    method: 'put',
    data: data
  })
}

// 删除车辆档案
export function delVehicle(vehicleId) {
  return request({
    url: '/system/vehicle/' + vehicleId,
    method: 'delete'
  })
}
