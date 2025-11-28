import request from '@/utils/request'

// 查询报警信息记录列表
export function streamStart(type, id, channel) {
  return request({
    url: '/api/camera/stream/start/' + type + "/" + id + "/" + channel,
    method: 'get'
  })
}
