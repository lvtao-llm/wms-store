import request from '@/utils/request'

// 查询车辆预约列表
export function listGatepass(query) {
  return request({
    url: '/system/gatepass/list',
    method: 'get',
    params: query
  })
}

// 查询车辆预约详细
export function getGatepass(gatepassId) {
  return request({
    url: '/system/gatepass/' + gatepassId,
    method: 'get'
  })
}

// 新增车辆预约
export function addGatepass(data) {
  return request({
    url: '/system/gatepass',
    method: 'post',
    data: data
  })
}

// 修改车辆预约
export function updateGatepass(data) {
  return request({
    url: '/system/gatepass',
    method: 'put',
    data: data
  })
}

// 删除车辆预约
export function delGatepass(gatepassId) {
  return request({
    url: '/system/gatepass/' + gatepassId,
    method: 'delete'
  })
}
