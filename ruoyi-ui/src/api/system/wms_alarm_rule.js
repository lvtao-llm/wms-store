import request from '@/utils/request'

// 查询报警信息规则列表
export function listWms_alarm_rule(query) {
  return request({
    url: '/system/wms_alarm_rule/list',
    method: 'get',
    params: query
  })
}

// 查询报警信息规则详细
export function getWms_alarm_rule(alarmRuleId) {
  return request({
    url: '/system/wms_alarm_rule/' + alarmRuleId,
    method: 'get'
  })
}

// 新增报警信息规则
export function addWms_alarm_rule(data) {
  return request({
    url: '/system/wms_alarm_rule',
    method: 'post',
    data: data
  })
}

// 修改报警信息规则
export function updateWms_alarm_rule(data) {
  return request({
    url: '/system/wms_alarm_rule',
    method: 'put',
    data: data
  })
}

// 删除报警信息规则
export function delWms_alarm_rule(alarmRuleId) {
  return request({
    url: '/system/wms_alarm_rule/' + alarmRuleId,
    method: 'delete'
  })
}
