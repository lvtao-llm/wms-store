import request from '@/utils/request'

// 查询设备列表
export function listWms_device(query) {
  return request({
    url: '/system/wms_device/list',
    method: 'get',
    params: query
  })
}

// 查询设备详细
export function getWms_device(id) {
  return request({
    url: '/system/wms_device/' + id,
    method: 'get'
  })
}

// 新增设备
export function addWms_device(data) {
  return request({
    url: '/system/wms_device',
    method: 'post',
    data: data
  })
}

// 修改设备
export function updateWms_device(data) {
  return request({
    url: '/system/wms_device',
    method: 'put',
    data: data
  })
}

// 删除设备
export function delWms_device(id) {
  return request({
    url: '/system/wms_device/' + id,
    method: 'delete'
  })
}
