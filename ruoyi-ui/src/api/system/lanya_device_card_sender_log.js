import request from '@/utils/request'

// 查询人脸发卡记录列表
export function listLanya_device_card_sender_log(query) {
  return request({
    url: '/system/lanya_device_card_sender_log/list',
    method: 'get',
    params: query
  })
}

export function listLanya_device_card_sender_log_by_name_card_type(query) {
  return request({
    url: '/system/lanya_device_card_sender_log/list-by-name-card-type',
    method: 'get',
    params: query
  })
}

// 查询人脸发卡记录详细
export function getLanya_device_card_sender_log(id) {
  return request({
    url: '/system/lanya_device_card_sender_log/' + id,
    method: 'get'
  })
}

// 新增人脸发卡记录
export function addLanya_device_card_sender_log(data) {
  return request({
    url: '/system/lanya_device_card_sender_log',
    method: 'post',
    data: data
  })
}

// 修改人脸发卡记录
export function updateLanya_device_card_sender_log(data) {
  return request({
    url: '/system/lanya_device_card_sender_log',
    method: 'put',
    data: data
  })
}

// 删除人脸发卡记录
export function delLanya_device_card_sender_log(id) {
  return request({
    url: '/system/lanya_device_card_sender_log/' + id,
    method: 'delete'
  })
}


