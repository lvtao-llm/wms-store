import request from '@/utils/request'

// 查询报警信息列表
export function listAlarm(query) {
  return request({
    url: '/system/alarm/list',
    method: 'get',
    params: query
  })
}

// 查询报警信息详细
export function getAlarm(alarmId) {
  return request({
    url: '/system/alarm/' + alarmId,
    method: 'get'
  })
}

// 新增报警信息
export function addAlarm(data) {
  return request({
    url: '/system/alarm',
    method: 'post',
    data: data
  })
}

// 修改报警信息
export function updateAlarm(data) {
  return request({
    url: '/system/alarm',
    method: 'put',
    data: data
  })
}

// 删除报警信息
export function delAlarm(alarmId) {
  return request({
    url: '/system/alarm/' + alarmId,
    method: 'delete'
  })
}
