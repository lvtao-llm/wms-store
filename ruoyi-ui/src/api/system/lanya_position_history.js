import request from '@/utils/request'

// 查询历史轨迹列表
export function listLanya_position_history(query) {
  return request({
    url: '/system/lanya_position_history/list',
    method: 'get',
    params: query
  })
}

// 查询历史轨迹详细
export function getLanya_position_history(id) {
  return request({
    url: '/system/lanya_position_history/' + id,
    method: 'get'
  })
}

// 新增历史轨迹
export function addLanya_position_history(data) {
  return request({
    url: '/system/lanya_position_history',
    method: 'post',
    data: data
  })
}

// 修改历史轨迹
export function updateLanya_position_history(data) {
  return request({
    url: '/system/lanya_position_history',
    method: 'put',
    data: data
  })
}

// 删除历史轨迹
export function delLanya_position_history(id) {
  return request({
    url: '/system/lanya_position_history/' + id,
    method: 'delete'
  })
}
