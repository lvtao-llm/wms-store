import request from '@/utils/request'

// 查询巡检任务列表
export function listWms_inspection_task(query) {
  return request({
    url: '/system/wms_inspection_task/list',
    method: 'get',
    params: query
  })
}

// 查询巡检任务详细
export function getWms_inspection_task(id) {
  return request({
    url: '/system/wms_inspection_task/' + id,
    method: 'get'
  })
}

// 新增巡检任务
export function addWms_inspection_task(data) {
  return request({
    url: '/system/wms_inspection_task',
    method: 'post',
    data: data
  })
}

// 修改巡检任务
export function updateWms_inspection_task(data) {
  return request({
    url: '/system/wms_inspection_task',
    method: 'put',
    data: data
  })
}

// 删除巡检任务
export function delWms_inspection_task(id) {
  return request({
    url: '/system/wms_inspection_task/' + id,
    method: 'delete'
  })
}
