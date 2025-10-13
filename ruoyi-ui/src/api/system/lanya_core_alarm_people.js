import request from '@/utils/request'

// 查询人员报警列表
export function listLanya_core_alarm_people(query) {
  return request({
    url: '/system/lanya_core_alarm_people/list',
    method: 'get',
    params: query
  })
}

// 查询人员报警详细
export function getLanya_core_alarm_people(id) {
  return request({
    url: '/system/lanya_core_alarm_people/' + id,
    method: 'get'
  })
}

// 新增人员报警
export function addLanya_core_alarm_people(data) {
  return request({
    url: '/system/lanya_core_alarm_people',
    method: 'post',
    data: data
  })
}

// 修改人员报警
export function updateLanya_core_alarm_people(data) {
  return request({
    url: '/system/lanya_core_alarm_people',
    method: 'put',
    data: data
  })
}

// 删除人员报警
export function delLanya_core_alarm_people(id) {
  return request({
    url: '/system/lanya_core_alarm_people/' + id,
    method: 'delete'
  })
}
