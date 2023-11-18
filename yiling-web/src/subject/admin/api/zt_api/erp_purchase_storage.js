import request from '@/subject/admin/utils/request'

// 中台-ERP流向数据-ERP采购入库数据集-采购入库仪表-获取采购商数据
export function requestPurchaseEnterpriseList(
  // 采购商类型
  channelId
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/purchase/enterprise/list',
    method: 'get',
    params: {
      channelId
    }
  })
}
// 中台-ERP流向数据-ERP采购入库数据集-采购入库仪表-获取供应商数据
export function requestSupplierEnterpriseList(
  // 供应商类型
  channelId
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/purchase/supplier/list',
    method: 'get',
    params: {
      channelId
    }
  })
}
// 中台-ERP流向数据-ERP采购入库数据集-采购入库仪表-采购商采购入库表
export function requestPurchaseList(
  // 采购年月，格式为yyyy-MM
  time,
  // 采购商业渠道类型id
  purchaseChannelId,
  // 采购商业Id列表
  purchaseEnterpriseIds,
  // 供应商渠道类型id
  supplierChannelId,
  // 供应商Id列表
  supplierEnterpriseIds
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/purchase/list',
    method: 'post',
    data: {
      time,
      purchaseChannelId,
      purchaseEnterpriseIds,
      supplierChannelId,
      supplierEnterpriseIds
    }
  })
}
// 中台-ERP流向数据-ERP采购入库数据集-采购入库仪表-采购入库明细表
export function requestPurchaseDetail(
  // 采购商ID
  purchaseEnterpriseId,
  // 供应商ID
  supplierEnterpriseId,
  // 月份
  time
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/purchase/detail',
    method: 'get',
    params: {
      purchaseEnterpriseId,
      supplierEnterpriseId,
      time
    }
  })
}
// 中台-ERP流向数据-ERP采购入库数据集-采购入库商品表
export function requestPurchaseGoodsList(
  // 采购年月
  time,
  // 采购商
  purchaseEnterpriseIds,
  // 商品名称
  goodsNameList,
  // 最小入库数量
  minQuantity,
  // 最大入库数量
  maxQuantity,
  // 省编码
  provinceCode,
  // 市编码
  cityCode,
  // 区编码
  regionCode
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/purchase/goods/list',
    method: 'post',
    data: {
      time,
      purchaseEnterpriseIds,
      goodsNameList,
      minQuantity,
      maxQuantity,
      provinceCode,
      cityCode,
      regionCode
    }
  })
}
// 中台-ERP流向数据-ERP采购入库数据集-采购入库商品表-采购商商品
export function requestPurchaseGoodsNameList() {
  return request({
    url: '/dataCenter/api/v1/report/flow/purchase/goodsName/list',
    method: 'post',
    data: {}
  })
}
