import request from '@/utils/request'

// 查询车辆报警记录列表
export function listLanya_vehicle_alarm(query) {
  return request({
    url: '/system/lanya_vehicle_alarm/list',
    method: 'get',
    params: query
  })
}

// 查询车辆报警记录详细
export function getLanya_vehicle_alarm(id) {
  return request({
    url: '/system/lanya_vehicle_alarm/' + id,
    method: 'get'
  })
}

// 新增车辆报警记录
export function addLanya_vehicle_alarm(data) {
  return request({
    url: '/system/lanya_vehicle_alarm',
    method: 'post',
    data: data
  })
}

// 修改车辆报警记录
export function updateLanya_vehicle_alarm(data) {
  return request({
    url: '/system/lanya_vehicle_alarm',
    method: 'put',
    data: data
  })
}

// 删除车辆报警记录
export function delLanya_vehicle_alarm(id) {
  return request({
    url: '/system/lanya_vehicle_alarm/' + id,
    method: 'delete'
  })
}
