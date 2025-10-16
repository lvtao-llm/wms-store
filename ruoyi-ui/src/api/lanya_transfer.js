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

// 获取绑定卡机详细
export function machineConfigRelationDetailByPerson(data) {
  return request({
    url: '/system/lanya-transfer/machine/config-relation/detail-by-person',
    method: 'post',
    data: data
  })
}

// 绑定卡机
export function machineConfigRelationEditByPerson(data) {
  return request({
    url: '/system/lanya-transfer/machine/config-relation/edit-by-person',
    method: 'post',
    data: data
  })
}

// 获取内部员工
export function listPersonStaffPage(data) {
  return request({
    url: '/system/lanya-transfer/person/staff/listPage',
    method: 'post',
    data: data
  })
}

// 新增内部员工
export function personStaffAddStaff(data) {
  return request({
    url: '/system/lanya-transfer/person/staff/addStaff',
    method: 'post',
    data: data
  })
}

// 修改内部员工
export function personStaffUpdateStaff(data) {
  return request({
    url: '/system/lanya-transfer/person/staff/updateStaff',
    method: 'post',
    data: data
  })
}

// 删除内部员工
export function personStaffDelStaff(data) {
  return request({
    url: '/system/lanya-transfer/person/staff/delStaff',
    method: 'post',
    data: data
  })
}

// 获取定位卡列表
export function deviceCardListPage(data) {
  return request({
    url: '/system/lanya-transfer/device/card/listPage',
    method: 'post',
    data: data
  })
}

// 新增定位卡
export function deviceCardAddCard(data) {
  return request({
    url: '/system/lanya-transfer/device/card/addCard',
    method: 'post',
    data: data
  })
}

// 修改定位卡
export function deviceCardUpdateCard(data) {
  return request({
    url: '/system/lanya-transfer/device/card/updateCard',
    method: 'post',
    data: data
  })
}

// 删除定位卡
export function deviceCardDelCard(data) {
  return request({
    url: '/system/lanya-transfer/device/card/delCard',
    method: 'post',
    data: data
  })
}

export function personInOutRecordPage(data) {
  return request({
    url: '/system/lanya-transfer/cardSender/cardSenderLog',
    method: 'post',
    data: data
  })
}

// 获取风险
export function systemRiskListPage(data) {
  return request({
    url: '/system/lanya-transfer/system/risk/listPage',
    method: 'post',
    data: data
  })
}

// 获取风险
export function systemRiskFindRisk(data) {
  return request({
    url: '/system/lanya-transfer/system/risk/findRisk',
    method: 'post',
    data: data
  })
}

// 新增风险
export function systemRiskAddRisk(data) {
  return request({
    url: '/system/lanya-transfer/system/risk/addRisk',
    method: 'post',
    data: data
  })
}

// 修改风险
export function systemRiskUpdateRisk(data) {
  return request({
    url: '/system/lanya-transfer/system/risk/updateRisk',
    method: 'post',
    data: data
  })
}

// 删除风险
export function systemRiskDelRisk(data) {
  return request({
    url: '/system/lanya-transfer/system/risk/delRisk',
    method: 'post',
    data: data
  })
}

// 层列表
export function deviceLayerListLayer(data) {
  return request({
    url: '/device/layer/listLayer',
    method: 'post',
    data: data
  })
}

// 定位卡预览
export function deviceBeaconPreview(data) {
  return request({
    url: '/device/beacon/preview',
    method: 'post',
    data: data
  })
}



