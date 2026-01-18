import request from '@/utils/request'

// 查询接料视图列表
export function listWms_access_log(query) {
  return request({
    url: '/system/access_log/list',
    method: 'get',
    params: query
  })
}
