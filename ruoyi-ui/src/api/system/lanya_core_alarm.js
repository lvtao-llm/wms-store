import request from '@/utils/request'

// 查询报警记录列表
export function listLanya_core_alarm(query) {
  return request({
    url: '/system/lanya_core_alarm/list',
    method: 'get',
    params: query
  })
}

// 查询报警记录详细
export function getLanya_core_alarm(alarmId) {
  return request({
    url: '/system/lanya_core_alarm/' + alarmId,
    method: 'get'
  })
}

// 新增报警记录
export function addLanya_core_alarm(data) {
  return request({
    url: '/system/lanya_core_alarm',
    method: 'post',
    data: data
  })
}

// 修改报警记录
export function updateLanya_core_alarm(data) {
  return request({
    url: '/system/lanya_core_alarm',
    method: 'put',
    data: data
  })
}

// 删除报警记录
export function delLanya_core_alarm(alarmId) {
  return request({
    url: '/system/lanya_core_alarm/' + alarmId,
    method: 'delete'
  })
}
