import request from '@/subject/pop/utils/request';

// 库存流向信息列表分页
export function getErpQueryGoodsBatchListPage(
  current,
  size,
  ename,
  goodsName,
  license,
  manufacturer,
  sourceList,
  provinceCode,
  cityCode,
  regionCode,
  enterpriseTagIdList,
  // 商品规格ID
  sellSpecificationsId,
  // 有无标准库规格关系：0-无, 1-有
  specificationIdFlag
) {
  return request({
    url: '/admin/pop/api/v1/flow/queryGoodsBatchListPage',
    method: 'post',
    data: {
      current,
      size,
      ename,
      goodsName,
      license,
      manufacturer,
      sourceList,
      provinceCode,
      cityCode,
      regionCode,
      enterpriseTagIdList,
      sellSpecificationsId,
      specificationIdFlag
    }
  });
}
// 企业销售流向信息列表分页
export function getErpQuerySaleListPage(
  current,
  size,
  ename,
  goodsName,
  enterpriseName,
  manufacturer,
  license,
  startTime,
  endTime,
  sourceList,
  provinceCode,
  cityCode,
  regionCode,
  enterpriseTagIdList,
  // 商品规格ID
  sellSpecificationsId,
  // 有无标准库规格关系：0-无, 1-有
  specificationIdFlag,
  //查询时间类型 0 : 6个月以内 1: 6个月以前
  timeType
) {
  return request({
    url: '/admin/pop/api/v1/flow/querySaleListPage',
    method: 'post',
    data: {
      current,
      size,
      ename,
      goodsName,
      enterpriseName,
      manufacturer,
      license,
      startTime,
      endTime,
      sourceList,
      provinceCode,
      cityCode,
      regionCode,
      enterpriseTagIdList,
      sellSpecificationsId,
      specificationIdFlag,
      timeType
    }
  });
}
// 企业采购流向信息列表分页
export function getErpQueryPurchaseListPage(
  current,
  size,
  ename,
  goodsName,
  enterpriseName,
  license,
  manufacturer,
  startTime,
  endTime,
  sourceList,
  provinceCode,
  cityCode,
  regionCode,
  enterpriseTagIdList,
  // 商品规格ID
  sellSpecificationsId,
  // 有无标准库规格关系：0-无, 1-有
  specificationIdFlag,
  //查询时间类型 0 : 6个月以内 1: 6个月以前
  timeType
) {
  return request({
    url: '/admin/pop/api/v1/flow/queryPurchaseListPage',
    method: 'post',
    data: {
      current,
      size,
      ename,
      goodsName,
      enterpriseName,
      license,
      manufacturer,
      startTime,
      endTime,
      sourceList,
      provinceCode,
      cityCode,
      regionCode,
      enterpriseTagIdList,
      sellSpecificationsId,
      specificationIdFlag,
      timeType
    }
  });
}

// 获取商家列表
export function queryEnterpriseList(ename) {
  return request({
    url: '/admin/pop/api/v1/flow/queryFlowEnterpriseList',
    method: 'post',
    data: {
      ename
    }
  });
}

// 获取企业标签选择项列表
export function getEnterpriseTagList() {
  return request({
    url: '/admin/pop/api/v1/flow/getEnterpriseTagList',
    method: 'get',
    params: {
    }
  });
}
// 通过规格名称获取规格ID
export function queryGoodsSpecification(key) {
  return request({
    url: '/admin/pop/api/v1/flow/queryGoodsSpecification',
    method: 'get',
    params: {
      key
    }
  });
}
// 连锁纯销流向信息列表分页
export function getErpqueryShopSaleListPage(
  current,
  size,
  // 总店名称
  ename,
  eid,
  // 门店商家eid
  shopEid,
  // 商品名称
  goodsName,
  // 批准文号
  license,
  // 销售时间
  startTime,
  endTime,
  // 生产厂家
  manufacturer,
  provinceCode,
  cityCode,
  regionCode,
  enterpriseTagIdList
) {
  return request({
    url: '/admin/pop/api/v1/flow/queryShopSaleListPage',
    method: 'post',
    data: {
      current,
      size,
      ename,
      eid,
      shopEid,
      goodsName,
      license,
      startTime,
      endTime,
      manufacturer,
      provinceCode,
      cityCode,
      regionCode,
      enterpriseTagIdList
    }
  });
}
// 获取总店/分店
export function queryFlowShopEnterpriseList(name, type) {
  return request({
    url: '/admin/pop/api/v1/flow/queryFlowShopEnterpriseList',
    method: 'post',
    data: {
      name,
      type
    }
  });
}