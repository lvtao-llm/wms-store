import request from '@/utils/request'

// 字典
export function list_dict_findData(query) {
  return request({
    url: '/system/lanya-transfer/dict/findData',
    method: 'get',
    params: query
  })
}
// 卡机列表
export function machine_list_page(query) {
  return request({
    url: '/system/lanya-transfer/machine/listPage',
    method: 'get',
    params: query
  })
}
