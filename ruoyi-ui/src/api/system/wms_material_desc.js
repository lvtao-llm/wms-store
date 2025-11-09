import request from '@/utils/request'

// 查询物料描述档案列表
export function listWms_material_desc(query) {
  return request({
    url: '/system/wms_material_desc/list',
    method: 'get',
    params: query
  })
}

// 查询物料描述档案详细
export function getWms_material_desc(materialDescId) {
  return request({
    url: '/system/wms_material_desc/' + materialDescId,
    method: 'get'
  })
}

// 新增物料描述档案
export function addWms_material_desc(data) {
  return request({
    url: '/system/wms_material_desc',
    method: 'post',
    data: data
  })
}

// 修改物料描述档案
export function updateWms_material_desc(data) {
  return request({
    url: '/system/wms_material_desc',
    method: 'put',
    data: data
  })
}

// 删除物料描述档案
export function delWms_material_desc(materialDescId) {
  return request({
    url: '/system/wms_material_desc/' + materialDescId,
    method: 'delete'
  })
}
