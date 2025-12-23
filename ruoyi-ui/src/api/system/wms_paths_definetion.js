import request from '@/utils/request'

// 查询虚拟路径点列表
export function listWms_paths_definetion(query) {
  return request({
    url: '/system/wms_paths_definetion/list',
    method: 'get',
    params: query
  })
}

// 查询虚拟路径点详细
export function getWms_paths_definetion(id) {
  return request({
    url: '/system/wms_paths_definetion/' + id,
    method: 'get'
  })
}

// 新增虚拟路径点
export function addWms_paths_definetion(data) {
  return request({
    url: '/system/wms_paths_definetion',
    method: 'post',
    data: data
  })
}

// 修改虚拟路径点
export function updateWms_paths_definetion(data) {
  return request({
    url: '/system/wms_paths_definetion',
    method: 'put',
    data: data
  })
}

// 删除虚拟路径点
export function delWms_paths_definetion(id) {
  return request({
    url: '/system/wms_paths_definetion/' + id,
    method: 'delete'
  })
}
