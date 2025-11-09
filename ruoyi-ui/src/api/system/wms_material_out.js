import request from '@/utils/request'

// 查询调拨视图列表
export function listWms_material_out(query) {
  return request({
    url: '/system/wms_material_out/list',
    method: 'get',
    params: query
  })
}

// 查询调拨视图详细
export function getWms_material_out(allotDetailId) {
  return request({
    url: '/system/wms_material_out/' + allotDetailId,
    method: 'get'
  })
}

// 新增调拨视图
export function addWms_material_out(data) {
  return request({
    url: '/system/wms_material_out',
    method: 'post',
    data: data
  })
}

// 修改调拨视图
export function updateWms_material_out(data) {
  return request({
    url: '/system/wms_material_out',
    method: 'put',
    data: data
  })
}

// 删除调拨视图
export function delWms_material_out(allotDetailId) {
  return request({
    url: '/system/wms_material_out/' + allotDetailId,
    method: 'delete'
  })
}
