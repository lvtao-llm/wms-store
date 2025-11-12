import request from '@/utils/request'

// 查询报警信息记录列表
export function streamStart(id) {
  return request({
    url: '/api/camera/stream/start/' + id,
    method: 'get'
  })
}
