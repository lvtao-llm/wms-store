import request from '@/utils/request'

// 查询区域点位全景列表
export function listWms_area_360(query) {
  return request({
    url: '/system/wms_area_360/list',
    method: 'get',
    params: query
  })
}

// 查询区域点位全景详细
export function getWms_area_360(id) {
  return request({
    url: '/system/wms_area_360/' + id,
    method: 'get'
  })
}

// 新增区域点位全景
export function addWms_area_360(data) {
  return request({
    url: '/system/wms_area_360',
    method: 'post',
    data: data
  })
}

// 修改区域点位全景
export function updateWms_area_360(data) {
  return request({
    url: '/system/wms_area_360',
    method: 'put',
    data: data
  })
}

// 删除区域点位全景
export function delWms_area_360(id) {
  return request({
    url: '/system/wms_area_360/' + id,
    method: 'delete'
  })
}
