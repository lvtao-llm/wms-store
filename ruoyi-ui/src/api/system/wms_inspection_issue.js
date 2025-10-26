import request from '@/utils/request'

// 查询巡检问题列表
export function listWms_inspection_issue(query) {
  return request({
    url: '/system/wms_inspection_issue/list',
    method: 'get',
    params: query
  })
}

// 查询巡检问题详细
export function getWms_inspection_issue(id) {
  return request({
    url: '/system/wms_inspection_issue/' + id,
    method: 'get'
  })
}

// 新增巡检问题
export function addWms_inspection_issue(data) {
  return request({
    url: '/system/wms_inspection_issue',
    method: 'post',
    data: data
  })
}

// 修改巡检问题
export function updateWms_inspection_issue(data) {
  return request({
    url: '/system/wms_inspection_issue',
    method: 'put',
    data: data
  })
}

// 删除巡检问题
export function delWms_inspection_issue(id) {
  return request({
    url: '/system/wms_inspection_issue/' + id,
    method: 'delete'
  })
}
