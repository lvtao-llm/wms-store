import request from '@/utils/request'

// 查询发卡记录列表
export function listRecord(query) {
  return request({
    url: '/system/record/list',
    method: 'get',
    params: query
  })
}

// 查询发卡记录详细
export function getRecord(cardRecordId) {
  return request({
    url: '/system/record/' + cardRecordId,
    method: 'get'
  })
}

// 新增发卡记录
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改发卡记录
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除发卡记录
export function delRecord(cardRecordId) {
  return request({
    url: '/system/record/' + cardRecordId,
    method: 'delete'
  })
}
