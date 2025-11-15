import request from '@/utils/request'

// 查询报警信息记录列表
export function streamStart(id, channel) {
  return request({
    url: '/api/camera/stream/start/' + id + "/" + channel,
    method: 'get'
  })
}
