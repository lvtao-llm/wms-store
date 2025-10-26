import request from '@/utils/request'

// 查询巡检规则列表
export function listWms_inspection_rule(query) {
  return request({
    url: '/system/wms_inspection_rule/list',
    method: 'get',
    params: query
  })
}

// 查询巡检规则详细
export function getWms_inspection_rule(id) {
  return request({
    url: '/system/wms_inspection_rule/' + id,
    method: 'get'
  })
}

// 新增巡检规则
export function addWms_inspection_rule(data) {
  return request({
    url: '/system/wms_inspection_rule',
    method: 'post',
    data: data
  })
}

// 修改巡检规则
export function updateWms_inspection_rule(data) {
  return request({
    url: '/system/wms_inspection_rule',
    method: 'put',
    data: data
  })
}

// 删除巡检规则
export function delWms_inspection_rule(id) {
  return request({
    url: '/system/wms_inspection_rule/' + id,
    method: 'delete'
  })
}
