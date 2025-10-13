import request from '@/utils/request'

// 查询发卡机列表
export function listLanya_device_machine(query) {
  return request({
    url: '/system/lanya_device_machine/list',
    method: 'get',
    params: query
  })
}

// 查询发卡机详细
export function getLanya_device_machine(cardSenderId) {
  return request({
    url: '/system/lanya_device_machine/' + cardSenderId,
    method: 'get'
  })
}

// 新增发卡机
export function addLanya_device_machine(data) {
  return request({
    url: '/system/lanya_device_machine',
    method: 'post',
    data: data
  })
}

// 修改发卡机
export function updateLanya_device_machine(data) {
  return request({
    url: '/system/lanya_device_machine',
    method: 'put',
    data: data
  })
}

// 删除发卡机
export function delLanya_device_machine(cardSenderId) {
  return request({
    url: '/system/lanya_device_machine/' + cardSenderId,
    method: 'delete'
  })
}
