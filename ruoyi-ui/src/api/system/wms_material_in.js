import request from '@/utils/request'

// 查询接料视图列表
export function listWms_material_in(query) {
  return request({
    url: '/system/wms_material_in/list',
    method: 'get',
    params: query
  })
}

// 查询接料视图详细
export function getWms_material_in(jlBh) {
  return request({
    url: '/system/wms_material_in/' + jlBh,
    method: 'get'
  })
}

// 新增接料视图
export function addWms_material_in(data) {
  return request({
    url: '/system/wms_material_in',
    method: 'post',
    data: data
  })
}

// 修改接料视图
export function updateWms_material_in(data) {
  return request({
    url: '/system/wms_material_in',
    method: 'put',
    data: data
  })
}

// 删除接料视图
export function delWms_material_in(jlBh) {
  return request({
    url: '/system/wms_material_in/' + jlBh,
    method: 'delete'
  })
}
