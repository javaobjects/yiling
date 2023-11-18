import request from '@/subject/pop/utils/request';

// 商品列表
export function getB2BProductList(
  current,
  size,
  name,
  licenseNo,
  manufacturer,
  outReason,
  goodsStatus,
  // 商品编码
  standardId,
  // 外部编码
  inSnOrSn
) {
  return request({
    url: '/admin/b2b/api/v1/goods/list',
    method: 'post',
    data: {
      current,
      size,
      name,
      licenseNo,
      manufacturer,
      outReason,
      goodsStatus,
      standardId,
      inSnOrSn
    }
  });
}
// 编辑商品
export function b2bProductEdit(
  //商品ID
  goodsId,
  //上下架
  goodsStatus,
  //基价
  price,
  //包装集合
  goodsSkuList,
  //生产日期
  manufacturingDate,
  //有效期
  expiryDate
) {
  return request({
    url: '/admin/b2b/api/v1/goods/update',
    method: 'post',
    data: {
      goodsId,
      goodsSkuList,
      goodsStatus,
      price,
      manufacturingDate,
      expiryDate
    }
  });
}
// 批量上下架
export function b2bProAllUpOrDown(goodsIds, goodsStatus) {
  return request({
    url: '/admin/b2b/api/v1/goods/updateStatus',
    method: 'post',
    data: {
      goodsIds,
      goodsStatus
    }
  });
}
// 商品详情
export function b2bProductDetail(goodsId) {
  return request({
    url: '/admin/b2b/api/v1/goods/detail',
    method: 'get',
    params: {
      goodsId
    }
  });
}

//  商品限价设置条件接口
export function b2bProSetPriceList(
  current,
  size,
  goodsId,
  customerType,
  provinceCode,
  cityCode,
  regionCode
) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/goodsPriceLimit',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      customerType,
      provinceCode,
      cityCode,
      regionCode
    }
  });
}
//  商品限价设置，新增  b2b后台控价模块-添加商品控价条件
export function b2bProSetPriceAdd(
  goodsId,
  customerType,
  provinceCode,
  cityCode,
  regionCode,
  price
) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/addGoodsPriceLimit',
    method: 'post',
    data: {
      cityCode,
      customerType,
      goodsId,
      provinceCode,
      regionCode,
      price
    }
  });
}
//  商品限价设置，新增  b2b后台控价模块-编辑商品控价条件
export function b2bProSetPriceUpdate(
  id,
  customerType,
  provinceCode,
  cityCode,
  regionCode,
  price
) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/updateGoodsPriceLimit',
    method: 'post',
    data: {
      cityCode,
      customerType,
      id,
      provinceCode,
      regionCode,
      price
    }
  });
}

//  商品设置，删除。b2b后台控价模块-移除商品控价条件

export function b2bProSetPriceDel(ids) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/deleteGoodsPriceLimit',
    method: 'post',
    data: {
      ids
    }
  });
}
//  商品设置，控销   b2b后台商品控销-获取区域控销

export function b2bProControlSellRegion(goodsId) {
  return request({
    url: '/admin/b2b/api/v1/control/getRegion',
    method: 'get',
    params: {
      goodsId
    }
  });
}
//  商品设置，控销   b2b后台商品控销-获取客户控销
export function b2bProControlSellCustomer(
  current,
  size,
  goodsId,
  name,
  type,
  customerGroupId,
  provinceCode,
  cityCode,
  regionCode
) {
  return request({
    url: '/admin/b2b/api/v1/control/getCustomer',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      name,
      type,
      customerGroupId,
      provinceCode,
      cityCode,
      regionCode
    }
  });
}
//  商品设置，控销   b2b后台商品控销-获取客户控销
export function b2bProControlSellPageCustomer(
  current,
  size,
  goodsId,
  name,
  type,
  customerGroupId,
  provinceCode,
  cityCode,
  regionCode
) {
  return request({
    url: '/admin/b2b/api/v1/control/pageCustomerList',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      name,
      type,
      customerGroupId,
      provinceCode,
      cityCode,
      regionCode
    }
  });
}

//  商品设置，控销   b2b后台商品控销-保存区域控销
export function b2bProControlSellSaveRegion(
  customerTypeSet,
  customerTypes,
  regionIds,
  regionSet,
  goodsId,
  controlDescribe
) {
  return request({
    url: '/admin/b2b/api/v1/control/saveRegion',
    method: 'post',
    data: {
      customerTypeSet,
      customerTypes,
      regionIds,
      regionSet,
      goodsId,
      controlDescribe
    }
  });
}
//  商品设置，控销   b2b后台商品控销-保存区域控销
export function b2bProControlSellSaveCustomer(
  customerSet,
  goodsId,
  customerId
) {
  return request({
    url: '/admin/b2b/api/v1/control/saveCustomer',
    method: 'post',
    data: {
      customerSet,
      goodsId,
      customerId
    }
  });
}
//  商品设置，控销   批量添加
export function b2bProControlSellAllAdd(
  current,
  size,
  goodsId,
  name,
  customerGroupId,
  type,
  provinceCode,
  cityCode,
  regionCode
) {
  return request({
    url: '/admin/b2b/api/v1/control/batchSaveCustomer',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      name,
      customerGroupId,
      type,
      provinceCode,
      cityCode,
      regionCode
    }
  });
}

//  商品设置，控销   批量添加
export function b2bProControlSellAllDel(conditionValue, controlId) {
  return request({
    url: '/admin/b2b/api/v1/control/deleteCustomer',
    method: 'post',
    data: {
      conditionValue,
      controlId
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

// 控价管理 ===========================================================================================模块接口
export function controlPriceList(
  current,
  size,
  customerGroupId,
  goodsId,
  name,
  type,
  provinceCode,
  cityCode,
  regionCode,
  tagName
) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/getCustomerList',
    method: 'post',
    data: {
      current,
      size,
      customerGroupId,
      goodsId,
      name,
      type,
      provinceCode,
      cityCode,
      regionCode,
      tagName
    }
  });
}
//  管控状态
export function setControlPriceStatus(customerEids, limitFlag) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/updateCustomerLimit',
    method: 'post',
    data: {
      customerEids,
      limitFlag
    }
  });
}

//  是否开启会员
export function setControlPriceRecommendationFlag(
  customerEids,
  recommendationFlag
) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/updateCustomerRecommendation',
    method: 'post',
    data: {
      customerEids,
      recommendationFlag
    }
  });
}

// //  编辑管控状体
// export function setControlPriceStatus(customerEids, limitFlag) {
//   return request({
//     url: "/admin/b2b/api/v1/limit/price/updateCustomerLimit",
//     method: "post",
//     data: {
//       customerEids,
//       limitFlag
//     }
//   });
// }

//  b2b后台控价模块-客户控价商品列表
export function ControlPriceListById(
  current,
  size,
  name,
  customerEid,
  licenseNo,
  manufacturer
) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/goodsList',
    method: 'post',
    data: {
      current,
      size,
      name,
      customerEid,
      licenseNo,
      manufacturer
    }
  });
}
// b2b后台控价模块-移除商品控价
export function ContorlPriceListDelet(customerEid, goodsIds) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/deleteGoodsLimit',
    method: 'post',
    data: {
      customerEid,
      goodsIds
    }
  });
}

// b2b后台控价模块 b2b后台商品弹框查询
export function ControlPriceGoodsListDialog(
  current,
  size,
  customerEid,
  goodsName,
  licenseNo,
  manufacturer,
  goodsId,
  promotionActivityId,
  eidList,
  from
) {
  return request({
    url: '/admin/b2b/api/v1/goods/b2bList',
    method: 'post',
    data: {
      current,
      size,
      customerEid,
      goodsName,
      licenseNo,
      manufacturer,
      goodsId,
      promotionActivityId,
      eidList,
      from
    }
  });
}
// b2b后台控价模块-添加商品控价条件
export function ControlPriceListAllAdd(
  current,
  size,
  goodsId,
  customerEid,
  goodsName,
  licenseNo
) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/batchAddGoodsLimit',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      customerEid,
      goodsName,
      licenseNo
    }
  });
}
// b2b后台控价模块-添加商品
export function ControlPriceListAdd(customerEid, goodsIds) {
  return request({
    url: '/admin/b2b/api/v1/limit/price/addGoodsLimit',
    method: 'post',
    data: {
      customerEid,
      goodsIds
    }
  });
}

// =====================b2b 商品定价模块===================================
// 商品定价首页列表
export function getPricePageList(
  current,
  size,
  goodsId,
  licenseNo,
  name,
  manufacturer
) {
  return request({
    url: '/admin/b2b/api/v1/price/pageList',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      // goodsStatus: !goodsStatus ? undefined : parseFloat(goodsStatus),
      licenseNo,
      name,
      manufacturer
    }
  });
}

// 客户定价客户列表
export function getUserPriceList(current, size, eid, goodsId) {
  return request({
    url: '/admin/b2b/api/v1/price/customer/pageList',
    method: 'post',
    data: {
      current,
      size,
      eid,
      goodsId
    }
  });
}

// 查询客户列表
export function getCustomerList(
  current,
  size,
  provinceCode,
  cityCode,
  regionCode,
  name,
  type,
  goodsId
) {
  return request({
    url: '/admin/b2b/api/v1/customer/pageList',
    method: 'post',
    data: {
      current,
      size,
      provinceCode,
      cityCode,
      regionCode,
      name,
      type,
      // 商品Id（调整客户定价/客户分组定价必传）
      goodsId
    }
  });
}

// 保存客户定价
export function saveUserPrice(
  customerEid,
  eid,
  goodsId,
  priceRule,
  priceValue
) {
  return request({
    url: '/admin/b2b/api/v1/price/customer/save',
    method: 'post',
    data: {
      customerEid,
      eid,
      goodsId,
      priceRule,
      priceValue
    }
  });
}

// 移除客户定价
export function delUserPrice(departmentId) {
  return request({
    url: '/admin/b2b/api/v1/price/customer/remove',
    method: 'get',
    params: {
      departmentId
    }
  });
}

// 客户定价分组列表
export function getUserPriceGroupList(current, size, eid, goodsId) {
  return request({
    url: '/admin/b2b/api/v1/price/customerGroup/pageList',
    method: 'post',
    data: {
      current,
      size,
      eid,
      goodsId
    }
  });
}

// 保存分组客户定价
export function saveUserGroupPrice(
  customerGroupId,
  eid,
  goodsId,
  priceRule,
  priceValue
) {
  return request({
    url: '/admin/b2b/api/v1/price/customerGroup/save',
    method: 'post',
    data: {
      customerGroupId,
      eid,
      goodsId,
      priceRule,
      priceValue
    }
  });
}

// 移除分组客户定价
export function delUserGroupPrice(departmentId) {
  return request({
    url: '/admin/b2b/api/v1/price/customerGroup/remove',
    method: 'get',
    params: {
      departmentId
    }
  });
}

// 查询客户分组列表
export function getCustomerGroupList(
  current,
  size,
  name,
  type,
  status,
  goodsId
) {
  return request({
    url: '/admin/b2b/api/v1/customerGroup/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      type,
      status,
      // 商品Id（调整客户定价/客户分组定价必传
      goodsId
    }
  });
}

// 取商品单独一个
export function getProductListOne(goodsId) {
  return request({
    url: '/admin/b2b/api/v1/goods/list',
    method: 'post',
    data: {
      goodsId
    }
  });
}

// 获取企业对接级别
export function getBussinessLevel() {
  return request({
    url: '/admin/b2b/api/v1/customer/erpSyncFlag',
    method: 'get',
    params: {}
  });
}
