import request from '@/utils/request'

// 查询报警信息记录列表
export function listWms_alarm_log(query) {
  return request({
    url: '/system/wms_alarm_log/list',
    method: 'get',
    params: query
  })
}

// 查询报警信息记录详细
export function getWms_alarm_log(alarmId) {
  return request({
    url: '/system/wms_alarm_log/' + alarmId,
    method: 'get'
  })
}

// 新增报警信息记录
export function addWms_alarm_log(data) {
  return request({
    url: '/system/wms_alarm_log',
    method: 'post',
    data: data
  })
}

// 修改报警信息记录
export function updateWms_alarm_log(data) {
  return request({
    url: '/system/wms_alarm_log',
    method: 'put',
    data: data
  })
}

// 删除报警信息记录
export function delWms_alarm_log(alarmId) {
  return request({
    url: '/system/wms_alarm_log/' + alarmId,
    method: 'delete'
  })
}
