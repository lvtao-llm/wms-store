import request from '@/utils/request'

// 查询定位卡管理列表
export function listLanya_device_card(query) {
  return request({
    url: '/system/lanya_device_card/list',
    method: 'get',
    params: query
  })
}

// 查询定位卡管理详细
export function getLanya_device_card(id) {
  return request({
    url: '/system/lanya_device_card/' + id,
    method: 'get'
  })
}

// 新增定位卡管理
export function addLanya_device_card(data) {
  return request({
    url: '/system/lanya_device_card',
    method: 'post',
    data: data
  })
}

// 修改定位卡管理
export function updateLanya_device_card(data) {
  return request({
    url: '/system/lanya_device_card',
    method: 'put',
    data: data
  })
}

// 删除定位卡管理
export function delLanya_device_card(id) {
  return request({
    url: '/system/lanya_device_card/' + id,
    method: 'delete'
  })
}
