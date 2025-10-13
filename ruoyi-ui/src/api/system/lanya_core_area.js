import request from '@/utils/request'

// 查询区域信息列表
export function listLanya_core_area(query) {
  return request({
    url: '/system/lanya_core_area/list',
    method: 'get',
    params: query
  })
}

// 查询区域信息详细
export function getLanya_core_area(areaId) {
  return request({
    url: '/system/lanya_core_area/' + areaId,
    method: 'get'
  })
}

// 新增区域信息
export function addLanya_core_area(data) {
  return request({
    url: '/system/lanya_core_area',
    method: 'post',
    data: data
  })
}

// 修改区域信息
export function updateLanya_core_area(data) {
  return request({
    url: '/system/lanya_core_area',
    method: 'put',
    data: data
  })
}

// 删除区域信息
export function delLanya_core_area(areaId) {
  return request({
    url: '/system/lanya_core_area/' + areaId,
    method: 'delete'
  })
}
