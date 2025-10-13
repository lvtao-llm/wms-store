import request from '@/utils/request'

// 查询访客记录列表
export function listLanya_core_visitor(query) {
  return request({
    url: '/system/lanya_core_visitor/list',
    method: 'get',
    params: query
  })
}

// 查询访客记录详细
export function getLanya_core_visitor(visitorId) {
  return request({
    url: '/system/lanya_core_visitor/' + visitorId,
    method: 'get'
  })
}

// 新增访客记录
export function addLanya_core_visitor(data) {
  return request({
    url: '/system/lanya_core_visitor',
    method: 'post',
    data: data
  })
}

// 修改访客记录
export function updateLanya_core_visitor(data) {
  return request({
    url: '/system/lanya_core_visitor',
    method: 'put',
    data: data
  })
}

// 删除访客记录
export function delLanya_core_visitor(visitorId) {
  return request({
    url: '/system/lanya_core_visitor/' + visitorId,
    method: 'delete'
  })
}
