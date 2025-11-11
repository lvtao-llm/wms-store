import request from '@/utils/request'

// 查询发送内容给卡播报列表
export function listWms_card_content_send(query) {
  return request({
    url: '/system/wms_card_content_send/list',
    method: 'get',
    params: query
  })
}

// 查询发送内容给卡播报详细
export function getWms_card_content_send(id) {
  return request({
    url: '/system/wms_card_content_send/' + id,
    method: 'get'
  })
}

// 新增发送内容给卡播报
export function addWms_card_content_send(data) {
  return request({
    url: '/system/wms_card_content_send',
    method: 'post',
    data: data
  })
}

// 修改发送内容给卡播报
export function updateWms_card_content_send(data) {
  return request({
    url: '/system/wms_card_content_send',
    method: 'put',
    data: data
  })
}

// 删除发送内容给卡播报
export function delWms_card_content_send(id) {
  return request({
    url: '/system/wms_card_content_send/' + id,
    method: 'delete'
  })
}
