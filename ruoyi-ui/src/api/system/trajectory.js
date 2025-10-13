import request from '@/utils/request'

// 查询轨迹列表
export function listTrajectory(query) {
  return request({
    url: '/system/trajectory/list',
    method: 'get',
    params: query
  })
}

// 查询轨迹详细
export function getTrajectory(trajectoryId) {
  return request({
    url: '/system/trajectory/' + trajectoryId,
    method: 'get'
  })
}

// 新增轨迹
export function addTrajectory(data) {
  return request({
    url: '/system/trajectory',
    method: 'post',
    data: data
  })
}

// 修改轨迹
export function updateTrajectory(data) {
  return request({
    url: '/system/trajectory',
    method: 'put',
    data: data
  })
}

// 删除轨迹
export function delTrajectory(trajectoryId) {
  return request({
    url: '/system/trajectory/' + trajectoryId,
    method: 'delete'
  })
}
