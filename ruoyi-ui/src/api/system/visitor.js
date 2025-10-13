import request from '@/utils/request'

// 查询访客信息列表
export function listVisitor(query) {
  return request({
    url: '/system/visitor/list',
    method: 'get',
    params: query
  })
}

// 查询访客信息详细
export function getVisitor(visitorId) {
  return request({
    url: '/system/visitor/' + visitorId,
    method: 'get'
  })
}

// 新增访客信息
export function addVisitor(data) {
  return request({
    url: '/system/visitor',
    method: 'post',
    data: data
  })
}

// 修改访客信息
export function updateVisitor(data) {
  return request({
    url: '/system/visitor',
    method: 'put',
    data: data
  })
}

// 删除访客信息
export function delVisitor(visitorId) {
  return request({
    url: '/system/visitor/' + visitorId,
    method: 'delete'
  })
}
