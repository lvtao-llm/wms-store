import request from '@/utils/request'

// 查询报警记录列表
export function listAlarm(query) {
  return request({
    url: '/lanya_core_alarm/alarm/list',
    method: 'get',
    params: query
  })
}

// 查询报警记录详细
export function getAlarm(alarmId) {
  return request({
    url: '/lanya_core_alarm/alarm/' + alarmId,
    method: 'get'
  })
}

// 新增报警记录
export function addAlarm(data) {
  return request({
    url: '/lanya_core_alarm/alarm',
    method: 'post',
    data: data
  })
}

// 修改报警记录
export function updateAlarm(data) {
  return request({
    url: '/lanya_core_alarm/alarm',
    method: 'put',
    data: data
  })
}

// 删除报警记录
export function delAlarm(alarmId) {
  return request({
    url: '/lanya_core_alarm/alarm/' + alarmId,
    method: 'delete'
  })
}
