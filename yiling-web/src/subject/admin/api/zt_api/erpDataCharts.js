import request from '@/subject/admin/utils/request'

// 中台-ERP流向数据-流向数据分析-采购单销售额报表
export function getlhPurchaseAmount(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/lhPurchaseAmount',
    method: 'post',
    data: {
      eid,
      startTime,
      endTime,
      goodsCategory,
      channelId,
      isCloudFlag
    }
  })
}
// 中台-ERP流向数据-流向数据分析-采购单销售数量报表
export function getlhPurchaseNumber(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓

) {
  return request({
    url: '/dataCenter/api/v1/report/flow/lhPurchaseNumber',
    method: 'post',
    data: {
      eid,
      startTime,
      endTime,
      goodsCategory,
      channelId,
      isCloudFlag
    }
  })
}
// 中台-ERP流向数据-流向数据分析-销售单销售额报表
export function getlhSaleAmount(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓

) {
  return request({
    url: '/dataCenter/api/v1/report/flow/lhSaleAmount',
    method: 'post',
    data: {
      eid,
      startTime,
      endTime,
      goodsCategory,
      channelId,
      isCloudFlag
    }
  })
}
// 中台-ERP流向数据-流向数据分析-销售单销售数量报表
export function getlhSaleNumber(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/lhSaleNumber',
    method: 'post',
    data: {
      eid,
      startTime,
      endTime,
      goodsCategory,
      channelId,
      isCloudFlag
    }
  })
}
// 中台-ERP流向数据-流向数据分析-ERP采购和POP发货对比
export function getStatisticsPopPurchase(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓

) {
  return request({
    url: '/dataCenter/api/v1/report/flow/statisticsPopPurchase',
    method: 'post',
    data: {
      eid,
      startTime,
      endTime,
      goodsCategory,
      channelId,
      isCloudFlag
    }
  })
}
// 中台-ERP流向数据-流向数据分析-库存销售数量报表
export function getGoodsBatchNumber(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/goodsBatchNumber',
    method: 'post',
    data: {
      eid,
      startTime,
      endTime,
      goodsCategory,
      channelId,
      isCloudFlag
    }
  })
}
// 中台-ERP流向数据-流向数据分析-搜索企业名称
export function queryEnterpriseList(
  name, //企业名称
  channelId // 渠道Id
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/enterpriseList',
    method: 'post',
    data: {
      name,
      channelId
    }
  })
}
// 中台-ERP采购流向-流向数据分析-采购流向列表
export function purchaseStatistics(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/purchaseStatistics',
    method: 'post',
    data: {
      eid, //商业公司编码
      startTime, //	开始时间
      endTime, //	结束时间
      goodsCategory, //商品品类:1连花2非连花
      channelId, // 渠道Id
      isCloudFlag
    }
  })
}
// 中台-ERP采购流向-流向数据分析-销售流向列表
export function saleStatistics(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/saleStatistics',
    method: 'post',
    data: {
      eid, //商业公司编码
      startTime, //	开始时间
      endTime, //	结束时间
      goodsCategory, //商品品类:1连花2非连花
      channelId, // 渠道Id
      isCloudFlag
    }
  })
}
// 中台-ERP采购流向-流向数据分析-库存列表
export function goodsBatchStatistics(
  eid, //商业公司编码
  startTime, //	开始时间
  endTime, //	结束时间
  goodsCategory, //商品品类:1连花2非连花
  channelId, // 渠道Id
  isCloudFlag // 是否云仓标识0全部1云仓
) {
  return request({
    url: '/dataCenter/api/v1/report/flow/goodsBatchStatistics',
    method: 'post',
    data: {
      eid, //商业公司编码
      startTime, //	开始时间
      endTime, //	结束时间
      goodsCategory, //商品品类:1连花2非连花
      channelId, // 渠道Id
      isCloudFlag
    }
  })
}
