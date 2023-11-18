import request from '@/subject/pop/utils/request'

//客户列表
export function getCustomerList(
  current,
  size,
  customerGroupId,
  name,
  customerCode,
  type,
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber//统一信用代码
) {
  return request({
    url: '/admin/b2b/api/v1/customer/pageList',
    method: 'post',
    data: {
      current,
      size,
      customerGroupId,
      name,
      customerCode,
      type,
      provinceCode,
      cityCode,
      regionCode,
      licenseNumber
    }
  })
}

// 开通线下支付
export function openOfflinePay(
  customerEid,
  //操作类型：1-开通 2-关闭
  opType
) {
  return request({
    url: '/admin/b2b/api/v1/customer/openOfflinePay',
    method: 'post',
    data: {
      customerEid,
      opType
    }
  })
}

// 查询结果开通线下支付
export function openOfflinePayByResult(
  name,
  provinceCode,
  cityCode,
  regionCode,
  licenseNumber,
  type,
  //操作类型：1-开通 2-关闭
  opType
) {
  return request({
    url: '/admin/b2b/api/v1/customer/openOfflinePayByResult',
    method: 'post',
    data: {
      name,
      provinceCode,
      cityCode,
      regionCode,
      licenseNumber,
      type,
      opType
    }
  })
}

// 获取单个客户信息
export function getCustomerDetail(customerEid, currentEid) {
  return request({
    url: '/admin/b2b/api/v1/customer/get',
    method: 'get',
    params: {
      customerEid,
      currentEid
    }
  })
}

// 修改客户信息
export function updateCustomer(customerEid, paymentMethodIds, customerGroupId) {
  return request({
    url: '/admin/b2b/api/v1/customer/update',
    method: 'post',
    data: {
      customerEid,
      paymentMethodIds,
      customerGroupId
    }
  })
}

//客户分组列表
export function getCustomerGroupList(
  current,
  size,
  status,//状态：0-全部 1-启用 2-停用
  type,//分组类型：0-全部 1-平台创建 2-ERP同步
  name
) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/pageList',
    method: 'post',
    data: {
      current,
      size,
      status,
      type,
      name
    }
  })
}

// 添加客户分组
export function addGroup(name) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/add',
    method: 'post',
    data: {
      name
    }
  })
}

// 删除客户分组
export function removeGroup(id) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/remove',
    method: 'get',
    params: {
      id
    }
  })
}

// 修改客户分组
export function updateGroup(id, name) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/update',
    method: 'post',
    data: {
      id,
      name
    }
  })
}

// 向分组中添加客户
export function addCustomers(groupId, customerEids) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/addCustomers',
    method: 'post',
    data: {
      groupId,
      customerEids
    }
  })
}

// 向分组中添加查询结果
export function addResultCustomers(
  groupId,//分组ID
  name,//客户名称
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber,//统一信用代码
  type//企业类型
) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/addResultCustomers',
    method: 'post',
    data: {
      groupId,//分组ID
      name,//客户名称
      provinceCode,//省
      cityCode,//市
      regionCode,//区
      licenseNumber,//统一信用代码
      type//企业类型
    }
  })
}

// 移除分组中客户
export function removeCustomers(groupId, customerEids) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/removeCustomers',
    method: 'post',
    data: {
      groupId,
      customerEids
    }
  })
}

// 移动分组客户
export function moveCustomers(originalGroupId, targetGroupId) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/moveCustomers',
    method: 'post',
    data: {
      originalGroupId,
      targetGroupId
    }
  })
}

// 申请管理列表
export function getPurchaseApply(
  current,
  size,
  name,
  type,
  authStatus,
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber//统一信用代码
) {
  return request({
    url: '/admin/b2b/api/v1/purchaseApply/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      type,
      authStatus,
      provinceCode,
      cityCode,
      regionCode,
      licenseNumber
    }
  })
}

// 申请管理-获取详情
export function getPurchaseApplyDetail(customerEid, currentEid) {
  return request({
    url: '/admin/b2b/api/v1/purchaseApply/geDetail',
    method: 'get',
    params: {
      customerEid,
      currentEid
    }
  })
}

// 申请管理-审核
export function updateAuthStatus(
  id,
  authStatus,
  authRejectReason
) {
  return request({
    url: '/admin/b2b/api/v1/purchaseApply/updateAuthStatus',
    method: 'post',
    data: {
      id,
      authStatus,
      authRejectReason
    },
    // 接口返回特殊处理
    noErr: true
  })
}
//刷新ERP客户编码
export function refreshErpCode(
  customerEid
) {
  return request({
    url: '/admin/b2b/api/v1/purchaseApply/refreshErpCode',
    method: 'post',
    data: {
      customerEid
    },
    //接口返回特殊处理
    timeout: 60000
  })
}
//查询erp编码
export function getErpCode(
  customerEid
) {
  return request({
    url: '/admin/b2b/api/v1/purchaseApply/getErpCode',
    method: 'get',
    params: {
      customerEid
    },
    // 接口返回特殊处理
    noErr: true
  })
}
//获取当前商户是否为ERP对接商户
export function getErpFlag() {
  return request({
    url: '/admin/b2b/api/v1/purchaseApply/getErpFlag',
    method: 'get',
    params: {}
  })
}