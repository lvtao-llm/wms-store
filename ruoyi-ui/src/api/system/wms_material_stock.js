import request from '@/utils/request'

// 查询库存视图列表
export function listWms_material_stock(query) {
  return request({
    url: '/system/wms_material_stock/list',
    method: 'get',
    params: query
  })
}

// 查询库存视图详细
export function getWms_material_stock(inventoryId) {
  return request({
    url: '/system/wms_material_stock/' + inventoryId,
    method: 'get'
  })
}

// 新增库存视图
export function addWms_material_stock(data) {
  return request({
    url: '/system/wms_material_stock',
    method: 'post',
    data: data
  })
}

// 修改库存视图
export function updateWms_material_stock(data) {
  return request({
    url: '/system/wms_material_stock',
    method: 'put',
    data: data
  })
}

// 删除库存视图
export function delWms_material_stock(inventoryId) {
  return request({
    url: '/system/wms_material_stock/' + inventoryId,
    method: 'delete'
  })
}
