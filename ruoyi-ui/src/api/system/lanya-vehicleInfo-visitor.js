import request from '@/utils/request'

// 查询访客车辆列表
export function listLanya_vehicleInfo_visitor(query) {
  return request({
    url: '/system/lanya-vehicleInfo-visitor/list',
    method: 'get',
    params: query
  })
}

// 查询访客车辆详细
export function getLanya_vehicleInfo_visitor(id) {
  return request({
    url: '/system/lanya-vehicleInfo-visitor/' + id,
    method: 'get'
  })
}

// 新增访客车辆
export function addLanya_vehicleInfo_visitor(data) {
  return request({
    url: '/system/lanya-vehicleInfo-visitor',
    method: 'post',
    data: data
  })
}

// 修改访客车辆
export function updateLanya_vehicleInfo_visitor(data) {
  return request({
    url: '/system/lanya-vehicleInfo-visitor',
    method: 'put',
    data: data
  })
}

// 删除访客车辆
export function delLanya_vehicleInfo_visitor(id) {
  return request({
    url: '/system/lanya-vehicleInfo-visitor/' + id,
    method: 'delete'
  })
}
