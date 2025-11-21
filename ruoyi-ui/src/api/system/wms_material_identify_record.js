import request from '@/utils/request'

// 查询物料识别记录列表
export function listWms_material_identify_record(query) {
  return request({
    url: '/system/wms_material_identify_record/list',
    method: 'get',
    params: query
  })
}

// 查询物料识别记录详细
export function getWms_material_identify_record(ymd) {
  return request({
    url: '/system/wms_material_identify_record/' + ymd,
    method: 'get'
  })
}

// 新增物料识别记录
export function addWms_material_identify_record(data) {
  return request({
    url: '/system/wms_material_identify_record',
    method: 'post',
    data: data
  })
}

// 修改物料识别记录
export function updateWms_material_identify_record(data) {
  return request({
    url: '/system/wms_material_identify_record',
    method: 'put',
    data: data
  })
}

// 删除物料识别记录
export function delWms_material_identify_record(ymd) {
  return request({
    url: '/system/wms_material_identify_record/' + ymd,
    method: 'delete'
  })
}
