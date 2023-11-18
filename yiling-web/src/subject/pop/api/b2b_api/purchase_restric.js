import request from '@/subject/pop/utils/request';

// 商品列表
export function queryGoodsPage(
  current,
    size,
    // 产品线：0 全部 1 pop 2 b2b
    goodsLine,
    // 商品名称
    name,
    // 注册证号
    licenseNo,
    // 生产厂家
    manufacturer,
    // 商品状态
    goodsStatus
) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/queryGoodsPage',
    method: 'post',
    data: {
      current,
      size,
      goodsLine,
      name,
      licenseNo,
      manufacturer,
      goodsStatus
    }
  });
}

// 获取详情及限购规则
export function getPurchaseRestriction(goodsId) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/getPurchaseRestriction',
    method: 'get',
    params: {
      goodsId
    }
  });
}

// 获取客户分组下拉信息
export function b2bProControlSellGroupList() {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/getCustomerGroupList',
    method: 'get',
    params: {}
  });
}

// 获取客户设置table列表
export function restrictionCustomerPage(
  current,
  size,
  // 客户分组ID
	customerGroupId,
  // 商品id
	goodsId,
  // 商品名称
	name,
  // 省
	provinceCode,
  // 市
  cityCode,
  // 区
	regionCode,
  // 客户类型
  type
) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/queryRestrictionCustomerPage',
    method: 'post',
    data: {
      current,
      size,
      customerGroupId,
      goodsId,
      name,
      provinceCode,
      cityCode,
      regionCode,
      type
    }
  });
}

// 单条删除限购客户
export function deleteRestrictionCustomer(
  // 客户eid
	customerEid,
  // 商品id
	goodsId
) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/deleteRestrictionCustomer',
    method: 'post',
    data: {
      customerEid,
      goodsId
    }
  });
}

// 批量删除限购客户
export function deleteAllRestrictionCustomer(
  current,
  size,
  // 商品id
  goodsId,
  // 客户分组
  customerGroupId,
  name,
  provinceCode,
  cityCode,
  regionCode,
  // 客户类型
	type
) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/batchDeleteRestrictionCustomerByQuery',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      customerGroupId,
      name,
      provinceCode,
      cityCode,
      regionCode,
      type
    }
  });
}

// 获取添加客户设置table列表
export function restrictionAddCustomerPage(
  current,
  size,
  // 客户分组ID
	customerGroupId,
  // 商品id
	goodsId,
  // 商品名称
	name,
  // 省
	provinceCode,
  // 市
  cityCode,
  // 区
	regionCode,
  // 客户类型
  type
) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/queryCustomerPage',
    method: 'post',
    data: {
      current,
      size,
      customerGroupId,
      goodsId,
      name,
      provinceCode,
      cityCode,
      regionCode,
      type
    }
  });
}

// 单条添加限购客户
export function addRestrictionCustomer(
  // 客户eid
	customerEid,
  // 商品id
	goodsId
) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/addRestrictionCustomer',
    method: 'post',
    data: {
      customerEid,
      goodsId
    }
  });
}

// 批量添加限购客户
export function batchAddRestrictionCustomerByQuery(
  current,
  size,
  // 商品id
  goodsId,
  // 客户分组
  customerGroupId,
  name,
  provinceCode,
  cityCode,
  regionCode,
  // 客户类型
	type
) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/batchAddRestrictionCustomerByQuery',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      customerGroupId,
      name,
      provinceCode,
      cityCode,
      regionCode,
      type
    }
  });
}

// 保存商品限购规则
export function savePurchaseRestriction(
  // 客户设置类型 0：全部客户 1:部分客户
  customerSettingType,
  // 商品id
	goodsId,
  // 限购id
	id,
  // 每单限购数量 0为无限制
	orderRestrictionQuantity,
  // 限购开始时间
	startTime,
  // 限购结束时间	
	endTime,
  // 时间内限购数量 0为无限制
	timeRestrictionQuantity,
  // 限购时间类型 1自定义 2 每天 3 每周 4每月
	timeType
) {
  return request({
    url: '/admin/b2b/api/v1/goodsPurchaseRestriction/savePurchaseRestriction',
    method: 'post',
    data: {
      customerSettingType,
      goodsId,
      id,
      orderRestrictionQuantity,
      startTime,
      endTime,
      timeRestrictionQuantity,
      timeType
    }
  });
}
