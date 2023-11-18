import request from '@/subject/admin/utils/request';

// 企业月份统计表
export function monthPageList(
  // 第几页，默认：1
  current,
  // 企业id
  eid,
  // 实施负责人
	installEmployee,
  // 结束数量
	maxQuantity,
  // 开始数量
	minQuantity,
  // 下拉类型(1-采购 2-销售 3-当前库存 4-平衡相差数)	
	quantityType,
  // 每页记录数，默认：10
	size,
  // 年月份(yyyy-MM)
	time
){
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/month/pageList',
    method: 'post',
    data: {
      current,
      eid,
      installEmployee,
      maxQuantity,
      minQuantity,
      quantityType,
      size,
      time
    }
  })
}
// 获取企业信息列表
export function enterpriseList() {
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/enterprise/list',
    method: 'get',
    params: {}
  })
}
// 获取企业每天的统计情况表
export function todayList(
  // 企业id
  eid,
  // 年月份(yyyy-MM)
	monthTime
) {
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/list',
    method: 'post',
    data: {
      eid,
      monthTime
    }
  })
}
// 获取商品统计详情 列表
export function statisticsList(
  // 企业id
	eid,
  // 查询月份 yyyy-MM
	monthTime,
  // 商品+规格id
	specificationId,
  // 第几页，默认：1
  current,
  // 每页记录数，默认：10
  size
) {
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/goodsSpec/statistics/list',
    method: 'post',
    data: {
      eid,
      monthTime,
      specificationId,
      current,
      size
    }
  })
}
// 获取商品规格条件列表
export function infoList(
  // 企业id
  eid,
  // 年月份(yyyy-MM)
  monthTime
) {
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/goodsSpec/info/list',
    method: 'get',
    params: {
      eid,
      monthTime
    }
  })
}
// 重新统计平衡数据
export function statistics(
  // 企业id
  eid,
  // 年月份(yyyy-MM)
  monthTime
) {
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/enterprise/again/statistics',
    method: 'get',
    params: {
      eid,
      monthTime
    }
  })
}
// 获取待匹配商品列表
export function noMatchedList(
  // 企业id
  eid,
  // 查询月份 yyyy-MM
  monthTime,
  // 商品+规格id
  specificationId
) {
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/goodsSpec/noMatched/list',
    method: 'post',
    data: {
      eid,
      monthTime,
      specificationId
    }
  })
}
// 获取推荐分数 
export function getRecommendScore(
  // 商品名称
  goodsName,
  // 商品规格
  spec,
  // 需要对比的商品名称
  targetGoodsName,
  // 需要对比的商品规格
  targetSpec,
  // 需要对比的商品厂家 
  targetManufacturer,
  // 厂家
  manufacturer	
) {
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/getRecommendScore',
    method: 'post',
    data: {
      goodsName,
      spec,
      targetGoodsName,
      targetSpec,
      targetManufacturer,
      manufacturer
    }
  })
}
// 保存匹配商品信息
export function specificationIdFlush(
  // 企业id
  eid,
  // 商品信息数组
  flushDataList
) {
  return request({
    url: '/erp/api/v1/flow/balanceStatistics/goodsSpec/specificationId/flush',
    method: 'post',
    data: {
      eid,
      flushDataList
    }
  })
}

