import request from '@/subject/pop/utils/request';

// 供应商详取商品列表
export function getCompanyProductList(
  current,
  size,
  // 下架原因
  outReason,
  // 产品状态
  goodsStatus,
  // 注册证号
  licenseNo,
  // 生产厂家
  manufacturer,
  name,
  // erp内码
  inSn,
  // 标准库一级分类id
  standardCategoryId1,
  // 标准库二级分类id
  standardCategoryId2
) {
  return request({
    url: '/admin/pop/api/v1/goods/list',
    method: 'post',
    data: {
      current,
      size,
      outReason,
      goodsStatus: goodsStatus == '0' ? undefined : parseFloat(goodsStatus),
      licenseNo,
      manufacturer,
      name,
      standardCategoryId1,
      standardCategoryId2,
      inSn
    }
  });
}
// 编辑商品
export function ProductEdit(goodsId, goodsStatus, price, goodsSkuList) {
  return request({
    url: '/admin/pop/api/v1/goods/edit',
    method: 'post',
    data: {
      goodsId,
      goodsSkuList,
      goodsStatus,
      price
    }
  });
}
// 查询商品明细
export function getProductDetail(goodsId) {
  return request({
    url: '/admin/pop/api/v1/goods/detail',
    method: 'get',
    params: {
      goodsId
    }
  });
}
// 库存明细
export function getInventoryDetail(skuId) {
  return request({
    url: '/admin/pop/api/v1/goods/getInventoryDetail',
    method: 'get',
    params: {
      skuId 
    }
  });
}
// 取商品单独一个
export function getProductListOne(goodsId) {
  return request({
    url: '/admin/pop/api/v1/goods/list',
    method: 'post',
    data: {
      goodsId
    }
  });
}

// 查询分类树
export function getProductCategory() {
  return request({
    url: '/admin/pop/api/v1/goods/getAll',
    method: 'post',
    data: {}
  });
}

// 查询公司列表
export function getCompanyList() {
  return request({
    url: '/admin/pop/api/v1/agreement/mainPart/list',
    method: 'get',
    params: {}
  });
}

// 修改商品状态
export function changeProductInfo(
  id,
  // 状态
  goodsStatus,
  // 包装规格
  bigPackage,
  // 价格
  price,
  // 库存
  qty,
  // 商品内码
  inSn = null,
  // 商品编码
  sn = null,
  // 商品专利
  isPatent,
  // 所属公司id
  eid,
  // 名称
  name,
  // 单位
  unit,
  // 生产厂家
  manufacturer,
  // 生产厂家地址
  manufacturerAddress,
  // 售卖规格
  specifications,
  // 批准文号
  licenseNo
) {
  return request({
    url: '/admin/pop/api/v1/goods/edit',
    method: 'post',
    data: {
      goodsStatus: goodsStatus == '0' ? undefined : parseFloat(goodsStatus),
      id,
      price,
      qty,
      bigPackage,
      inSn,
      sn,
      isPatent,
      eid,
      name,
      unit,
      manufacturer,
      manufacturerAddress,
      specifications,
      licenseNo
    }
  });
}

// 批量上下架
export function updateStatusAll(goodsIds, goodsStatus) {
  return request({
    url: '/admin/pop/api/v1/goods/updateStatus',
    method: 'post',
    data: {
      goodsIds,
      goodsStatus: goodsStatus == '0' ? undefined : parseFloat(goodsStatus)
    }
  });
}

// 商品审核记录列表
export function getCheckList(current, size, goodsId, source) {
  return request({
    url: '/admin/pop/api/v1/goods/auditList',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      source
    }
  });
}

// 商品定价首页列表
export function getPricePageList(
  current,
  size,
  goodsId,
  goodsStatus,
  licenseNo,
  name,
  manufacturer
) {
  return request({
    url: '/admin/pop/api/v1/price/pageList',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      goodsStatus: !goodsStatus ? undefined : parseFloat(goodsStatus),
      licenseNo,
      name,
      manufacturer
    }
  });
}

// 商品日志列表
export function getLogList(
  current,
  size,
  name,
  gid,
  licenseNo,
  modifyColumn,
  operUser,
  startTime,
  endTime
) {
  return request({
    url: '/admin/pop/api/v1/goods/getGoodsLog',
    method: 'post',
    data: {
      current,
      size,
      name,
      gid,
      licenseNo,
      modifyColumn,
      operUser,
      startTime,
      endTime
    }
  });
}

// 客户定价客户列表
export function getUserPriceList(current, size, eid, goodsId) {
  return request({
    url: '/admin/pop/api/v1/price/customer/pageList',
    method: 'post',
    data: {
      current,
      size,
      eid,
      goodsId
    }
  });
}

// 客户定价分组列表
export function getUserPriceGroupList(current, size, eid, goodsId) {
  return request({
    url: '/admin/pop/api/v1/price/customerGroup/pageList',
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
    url: '/admin/pop/api/v1/customer/pageList',
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
    url: '/admin/pop/api/v1/price/customer/save',
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
    url: '/admin/pop/api/v1/price/customer/remove',
    method: 'get',
    params: {
      departmentId
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
    url: '/admin/pop/api/v1/price/customerGroup/save',
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
    url: '/admin/pop/api/v1/price/customerGroup/remove',
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
    url: '/admin/pop/api/v1/customer/group/pageList',
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

// 超卖商品管理列表
export function getOverSoldList(
  current,
  size,
  //  商家名称
  name,
  // 批准文号
  licenseNo,
  // 生产厂家
  manufacturer,
  //  是否超卖 0-非超卖 1-超卖
  overSoldType
) {
  return request({
    url: '/admin/pop/api/v1/goods/overSoldList',
    method: 'post',
    data: {
      current,
      size,
      name,
      licenseNo,
      manufacturer,
      overSoldType
    }
  });
}

// 添加超卖商品弹窗列表
export function getOverSoldPopList(
  current,
  size,
  //  商品名称
  name,
  // 批准文号
  licenseNo,
  // 生产厂家
  manufacturer
) {
  return request({
    url: '/admin/pop/api/v1/goods/overSoldPopList',
    method: 'post',
    data: {
      current,
      size,
      name,
      licenseNo,
      manufacturer
    }
  });
}

// 添加移除超卖商品
export function updateOverSold(
  //  操作的商品id数组集合
  goodsIds,
  //  操作类型 0-移除 1-添加
  overSoldType
) {
  return request({
    url: '/admin/pop/api/v1/goods/updateOverSold',
    method: 'post',
    data: {
      goodsIds,
      overSoldType
    }
  });
}

// 获取以岭主体企业列表
export function getEnterpriseMainPart() {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/mainPart/list',
    method: 'get'
  });
}

// 保存订阅关系
export function mainPartList(
  eid,
  inventoryId,
  skuId,
  subscriptionList
) {
  return request({
    url: '/admin/pop/api/v1/goods/saveInventorySubscription',
    method: 'post',
    data: {
      eid,
      inventoryId,
      skuId,
      subscriptionList
    }
  });
}

// 获取订阅列表
export function getSubscriptionList(skuId) {
  return request({
    url: '/admin/pop/api/v1/goods/getSubscriptionList',
    method: 'get',
    params: {
      skuId
    }
  });
}

// 保存或修改限价信息
export function saveOrUpdateLimitPrice(
  // 商品ID
  goodsId,
  id,
  // 价格下限
  lowerLimitPrice,
  // 价格上限	
  upperLimitPrice
) {
  return request({
    url: '/admin/pop/api/v1/price/limit/saveOrUpdateLimitPrice',
    method: 'POST',
    data: {
      goodsId,
      id,
      lowerLimitPrice,
      upperLimitPrice
    }
  });
}

// 获取限价信息
export function getLimitPrice(
  // 商品ID
  goodsId
) {
  return request({
    url: '/admin/pop/api/v1/price/limit/getLimitPrice',
    method: 'get',
    params: {
      goodsId
    }
  });
}

// 查询商品信息定价
export function getInfo(
  // 商品ID
  goodsId
) {
  return request({
    url: '/admin/pop/api/v1/goods/getInfo',
    method: 'get',
    params: {
      goodsId
    }
  });
}

// 推荐商品组分页
export function recommendGoodsGroupPage(
  current,
  size,
  // 分组名称
  name,
  // 是否启用快速采购推荐位 0：开启 1：关闭
  quickPurchaseFlag
) {
  return request({
    url: '/admin/pop/api/v1/recommendGoodsGroup/groupPage',
    method: 'post',
    data: {
      current,
      size,
      name,
      quickPurchaseFlag
    }
  });
}

// pop后台商品弹框查询
export function getGoodsPopList(
  current,
  size,
  // 商品名称
  goodsName,
  // 批准文号
  licenseNo,
  // 生产厂家
  manufacturer
) {
  return request({
    url: '/admin/pop/api/v1/goods/popList',
    method: 'post',
    data: {
      current,
      size,
      goodsName,
      licenseNo,
      manufacturer
    }
  })
}

// 保存或修改商品推荐组
export function recommendGoodsGroupSaveOrUpdate(
  // 商品id
  id,
  // 商品名称
  name,
  // 是否启用快速采购推荐位 0：开启 1：关闭
  quickPurchaseFlag,
  // 关联商品列表
  relationList
) {
  return request({
    url: '/admin/pop/api/v1/recommendGoodsGroup/saveOrUpdate',
    method: 'post',
    data: {
      id,
      name,
      quickPurchaseFlag,
      relationList
    }
  })
}

// id获取组信息(含关联商品信息)
export function recommendGoodsGroupGetGroup(groupId) {
  return request({
    url: '/admin/pop/api/v1/recommendGoodsGroup/getGroup',
    method: 'get',
    params: {
      groupId
    }
  })
}

// 删除推荐组
export function recommendGoodsGroupDeleteGroup(
  // 商品组id
  groupId
) {
  return request({
    url: '/admin/pop/api/v1/recommendGoodsGroup/deleteGroup',
    method: 'post',
    data: {
      groupId
    }
  })
}

