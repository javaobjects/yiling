import request from '@/subject/pop/utils/request'

// 供应商详取商品列表
export function getChannelList(
  current,
  size,
  name,//渠道商名称
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber,//统一信用代码
  channelId,//渠道类型
  contactUserName,//商务联系人
  paymentMethodScope,//支付方式
  purchaseRelationScope,//采购关系
  customerContactScope,//商务负责人
  customerGroupId//分组ID
) {
  return request({
    url: '/admin/pop/api/v1/channel/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,//渠道商名称
      provinceCode,//省
      cityCode,//市
      regionCode,//区
      licenseNumber,//统一信用代码
      channelId,//渠道类型
      contactUserName,//商务联系人
      paymentMethodScope,//支付方式
      purchaseRelationScope,//采购关系
      customerContactScope,//商务负责人
      customerGroupId
    }
  })
}

// 获取分组下的客户列表
export function getGrouplcustomerList(
  current,
  size,
  customerGroupId,//分组ID
  name,//渠道商名称
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber,//统一信用代码
  channelId,//渠道类型
  type//企业类型
) {
  return request({
    url: '/admin/pop/api/v1/channel/pageList',
    method: 'post',
    data: {
      current,
      size,
      customerGroupId,//分组ID
      name,//渠道商名称
      provinceCode,//省
      cityCode,//市
      regionCode,//区
      licenseNumber,//统一信用代码
      channelId,//渠道类型
      type//企业类型
    }
  })
}

// 统计渠道商采购销购销售渠道商的类型及个数
export function getChannelCount(buyerEid) {
  return request({
    url: '/admin/pop/api/v1/purchase/relation/countSellerChannel',
    method: 'post',
    data: {
      buyerEid
    }
  })
}

// 查询渠道商采购关系信息分页列表
export function getPurchaseList(
  current,
  size,
  sellerChannelId,//渠道类型
  buyerEid
) {
  return request({
    url: '/admin/pop/api/v1/purchase/relation/pageList',
    method: 'post',
    data: {
      current,
      size,
      sellerChannelId,
      buyerEid
    }
  })
}

// 获取单个渠道商信息
export function getChannelDetail(customerEid) {
  return request({
    url: '/admin/pop/api/v1/channel/get',
    method: 'get',
    params: {
      customerEid
    }
  })
}

// 新增EAS信息
export function addEasInfo(
  customerEid,
  easName,
  easCode
) {
  return request({
    url: '/admin/pop/api/v1/channel/addEasInfo',
    method: 'post',
    data: {
      customerEid,
      easName,
      easCode
    }
  })
}

// 查询商务联系人分页列表
export function getContactList(
  current,
  size,
  status,// 状态：0-全部 1-启用 2-停用
  name,
  mobile
) {
  return request({
    url: '/admin/dataCenter/api/v1/employee/pageList',
    method: 'post',
    data: {
      current,
      size,
      status,
      name,
      mobile
    }
  })
}

//部门分页列表
export function getEnterpriseList(
  current,
  size,
  status,
  eid
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/department/pageList',
    method: 'post',
    data: {
      current,
      size,
      status,
      eid
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
    url: '/admin/pop/api/v1/customer/group/pageList',
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

// 保存渠道商信息
export function saveChannel(contactUserIds, customerEid, customerGroupId, paymentMethodIds, eid) {
  return request({
    url: '/admin/pop/api/v1/channel/save',
    method: 'post',
    data: {
      contactUserIds,
      customerEid,
      customerGroupId,
      paymentMethodIds,
      eid
    }
  })
}

// 查询渠道商可供采购企业分页列表
export function getCanPurchaseEnterprisePageList(
  current,
  size,
  buyerChannelId,
  buyerEid,
  sellerChannelId,
  sellerName
) {
  return request({
    url: '/admin/pop/api/v1/purchase/relation/canPurchaseEnterprisePageList',
    method: 'post',
    data: {
      current,
      size,
      buyerChannelId,
      buyerEid,
      sellerChannelId,
      sellerName
    }
  })
}

// 添加采购关系
export function addPurchaseRelation(
  buyerId,
  sellerIds,
  source
) {
  return request({
    url: '/admin/pop/api/v1/purchase/relation/addPurchaseRelation',
    method: 'post',
    data: {
      buyerId,
      sellerIds,
      source
    }
  })
}

// 移除采购关系
export function removePurchaseRelation(
  buyerId,
  sellerIds
) {
  return request({
    url: '/admin/pop/api/v1/purchase/relation/removePurchaseRelation',
    method: 'post',
    data: {
      buyerId,
      sellerIds
    }
  })
}

// 客户分页列表
export function getCustomerList(
  current,
  size,
  type,//客户类型
  name,//渠道商名称
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber//统一信用代码
) {
  return request({
    url: '/admin/pop/api/v1/customer/pageList',
    method: 'post',
    data: {
      current,
      size,
      type,
      name,//渠道商名称
      provinceCode,//省
      cityCode,//市
      regionCode,//区
      licenseNumber//统一信用代码
    }
  })
}

// 获取单个客户信息
export function getCustomerDetail(customerEid, currentEid) {
  return request({
    url: '/admin/pop/api/v1/customer/get',
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
    url: '/admin/pop/api/v1/customer/update',
    method: 'post',
    data: {
      customerEid,
      paymentMethodIds,
      customerGroupId
    }
  })
}

// 添加客户分组
export function addGroup(name) {
  return request({
    url: '/admin/pop/api/v1/customer/group/add',
    method: 'post',
    data: {
      name
    }
  })
}

// 删除客户分组
export function removeGroup(id) {
  return request({
    url: '/admin/pop/api/v1/customer/group/remove',
    method: 'get',
    params: {
      id
    }
  })
}

// 修改客户分组
export function updateGroup(id, name) {
  return request({
    url: '/admin/pop/api/v1/customer/group/update',
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
    url: '/admin/pop/api/v1/customer/group/addCustomers',
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
  channelId,//渠道类型
  type//企业类型
) {
  return request({
    url: '/admin/pop/api/v1/customer/group/addResultCustomers',
    method: 'post',
    data: {
      groupId,//分组ID
      name,//客户名称
      provinceCode,//省
      cityCode,//市
      regionCode,//区
      licenseNumber,//统一信用代码
      channelId,//渠道类型
      type//企业类型
    }
  })
}

// 移除分组中客户
export function removeCustomers(groupId, customerEids) {
  return request({
    url: '/admin/pop/api/v1/customer/group/removeCustomers',
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
    url: '/admin/pop/api/v1/customer/group/moveCustomers',
    method: 'post',
    data: {
      originalGroupId,
      targetGroupId
    }
  })
}

// 删除EAS信息
export function deleteEasInfo(id) {
  return request({
    url: '/admin/pop/api/v1/channel/deleteEasInfo',
    method: 'post',
    data: {
      id
    }
  })
}