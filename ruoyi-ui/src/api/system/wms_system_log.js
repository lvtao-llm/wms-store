import request from '@/utils/request'

// 查询定时任务调度日志列表
export function listWms_system_log(query) {
  return request({
    url: '/system/wms_system_log/list',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度日志详细
export function getWms_system_log(jobLogId) {
  return request({
    url: '/system/wms_system_log/' + jobLogId,
    method: 'get'
  })
}

// 新增定时任务调度日志
export function addWms_system_log(data) {
  return request({
    url: '/system/wms_system_log',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度日志
export function updateWms_system_log(data) {
  return request({
    url: '/system/wms_system_log',
    method: 'put',
    data: data
  })
}

// 删除定时任务调度日志
export function delWms_system_log(jobLogId) {
  return request({
    url: '/system/wms_system_log/' + jobLogId,
    method: 'delete'
  })
}
