import request from '@/utils/request'

// 查询摄像头识别日志列表
export function listWms_device_camera_log(query) {
  return request({
    url: '/system/wms_device_camera_log/list',
    method: 'get',
    params: query
  })
}

// 查询摄像头识别日志详细
export function getWms_device_camera_log(id) {
  return request({
    url: '/system/wms_device_camera_log/' + id,
    method: 'get'
  })
}

// 新增摄像头识别日志
export function addWms_device_camera_log(data) {
  return request({
    url: '/system/wms_device_camera_log',
    method: 'post',
    data: data
  })
}

// 修改摄像头识别日志
export function updateWms_device_camera_log(data) {
  return request({
    url: '/system/wms_device_camera_log',
    method: 'put',
    data: data
  })
}

// 删除摄像头识别日志
export function delWms_device_camera_log(id) {
  return request({
    url: '/system/wms_device_camera_log/' + id,
    method: 'delete'
  })
}
