import request from '@/utils/request'

// 查询内部员工列表
export function listLanya_internal_employee(query) {
  return request({
    url: '/system/lanya_internal_employee/list',
    method: 'get',
    params: query
  })
}

// 查询内部员工详细
export function getLanya_internal_employee(personId) {
  return request({
    url: '/system/lanya_internal_employee/' + personId,
    method: 'get'
  })
}

// 新增内部员工
export function addLanya_internal_employee(data) {
  return request({
    url: '/system/lanya_internal_employee',
    method: 'post',
    data: data
  })
}

// 修改内部员工
export function updateLanya_internal_employee(data) {
  return request({
    url: '/system/lanya_internal_employee',
    method: 'put',
    data: data
  })
}

// 删除内部员工
export function delLanya_internal_employee(personId) {
  return request({
    url: '/system/lanya_internal_employee/' + personId,
    method: 'delete'
  })
}
