import request from '@/utils/request'

// 查询人员（员工/访客/承包商人员）列表
export function listLanya_core_person(query) {
  return request({
    url: '/system/lanya_core_person/list',
    method: 'get',
    params: query
  })
}

// 查询人员（员工/访客/承包商人员）详细
export function getLanya_core_person(personId) {
  return request({
    url: '/system/lanya_core_person/' + personId,
    method: 'get'
  })
}

// 新增人员（员工/访客/承包商人员）
export function addLanya_core_person(data) {
  return request({
    url: '/system/lanya_core_person',
    method: 'post',
    data: data
  })
}

// 修改人员（员工/访客/承包商人员）
export function updateLanya_core_person(data) {
  return request({
    url: '/system/lanya_core_person',
    method: 'put',
    data: data
  })
}

// 删除人员（员工/访客/承包商人员）
export function delLanya_core_person(personId) {
  return request({
    url: '/system/lanya_core_person/' + personId,
    method: 'delete'
  })
}
