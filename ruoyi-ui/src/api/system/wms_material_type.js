import request from '@/utils/request'

// 查询物料类型列表
export function listWms_material_type(query) {
  return request({
    url: '/system/wms_material_type/list',
    method: 'get',
    params: query
  })
}

// 查询物料类型详细
export function getWms_material_type(bm) {
  return request({
    url: '/system/wms_material_type/' + bm,
    method: 'get'
  })
}

// 新增物料类型
export function addWms_material_type(data) {
  return request({
    url: '/system/wms_material_type',
    method: 'post',
    data: data
  })
}

// 修改物料类型
export function updateWms_material_type(data) {
  return request({
    url: '/system/wms_material_type',
    method: 'put',
    data: data
  })
}

// 删除物料类型
export function delWms_material_type(bm) {
  return request({
    url: '/system/wms_material_type/' + bm,
    method: 'delete'
  })
}
