/*
 * @Description:
 * @Author: xuxingwang
 * @Date: 2022-02-24 18:00:14
 * @LastEditTime: 2022-02-25 17:45:06
 * @LastEditors: xuxingwang
 */
import request from '@/subject/admin/utils/request';

// 获取参数报表
export function getReportParamsList(current, size) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryReportParamPage',
    method: 'get',
    params: {
      current,
      size
    }
  });
}
// 获取商品类型列表
export function getGoodsTypeList(current, size) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryReportGoodsCategoryPage',
    method: 'get',
    params: {
      current,
      size
    }
  });
}

//  获取商品类型，促销活动，阶梯设置，会员返利 列表接口

export function getParamsTypeList(
  current,
  size,
  parType,
  paramId,
  eid,
  thresholdAmount,
  memberSource,
  userName,
  startUpdateTime,
  endUpdateTime,
  // 会员名称(id)
  memberId
) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryReportParamSubPageList',
    method: 'post',
    data: {
      current,
      size,
      // 1-商品类型 2-促销活动 3-阶梯规则 4-会员返利
      parType,
      paramId,
      eid,
      thresholdAmount,
      memberSource,
      userName,
      startUpdateTime,
      endUpdateTime,
      memberId
    }
  });
}

// 添加商品类型 和 会员类型
export function addGoodsAndmMemberType(paramId, parType, name) {
  return request({
    url: '/dataCenter/api/v1/report/param/addReportParamSub',
    method: 'post',
    data: {
      paramId,
      parType, // 	参数类型：1-商品类型 4-会员返利
      name
    }
  });
}
// 修改商品类型
export function editGoodsAndMemberType(paramId, parType, name, id) {
  return request({
    url: '/dataCenter/api/v1/report/param/updateReportParamSub',
    method: 'post',
    data: {
      paramId,
      parType, // 	参数类型：1-商品类型 4-会员返利
      name,
      id // 子参数id
    }
  });
}
// 添加会员类型
export function addMemberType(
  paramId,
  parType,
  eid,
  thresholdAmount,
  rewardValue,
  rewardType,
  memberSource,
  startTime,
  endTime,
  // 会员参数的会员id--添加会员时必填
  memberId
) {
  return request({
    url: '/dataCenter/api/v1/report/param/addReportParamSub',
    method: 'post',
    data: {
      paramId,
      parType, // 	参数类型：1-商品类型 4-会员返利
      eid,
      thresholdAmount,
      rewardValue,
      rewardType,
      memberSource,
      startTime,
      endTime,
      memberId
    }
  });
}

// 修改会员类型
export function editMemberType(
  id,
  parType,
  eid,
  thresholdAmount,
  rewardValue,
  rewardType,
  memberSource,
  startTime,
  endTime,
  // 会员参数的会员id--添加会员时必填
  memberId
) {
  return request({
    url: '/dataCenter/api/v1/report/param/updateReportParamSub',
    method: 'post',
    data: {
      id,
      parType, // 	参数类型：1-商品类型 4-会员返利
      eid,
      thresholdAmount,
      rewardValue,
      rewardType,
      memberSource,
      startTime,
      endTime,
      memberId
    }
  });
}

// 商品类型下模糊查询商品列表
export function getGoodsList(key) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryGoods',
    method: 'get',
    params: {
      key
    }
  });
}
// 商家对接以岭类型下模糊查询以岭商品列表
export function getYLGoodsList(key) {
  return request({
    url: '/dataCenter/api/v1/report/param/flowGoodsRelation/getYlGoodsList',
    method: 'get',
    params: {
      key
    }
  });
}

// 活动和阶梯下 模糊查询商品列表

export function getActivityGoodsList(eid, goodsName) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryActivityGoods',
    method: 'get',
    params: {
      eid,
      goodsName
    }
  });
}

// 根据企业名称查询企业信息
export function getBussinessInfoByBussinessName(current, size, name) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryEntInfoPageList',
    method: 'post',
    data: {
      current,
      size,
      name
    }
  });
}

//  在商品类型下添加商品
export function addGoods(
  paramSubId,
  goodsName,
  ylGoodsId,
  goodsSpecification,
  startTime,
  endTime
) {
  return request({
    url: '/dataCenter/api/v1/report/param/addCategoryGoods',
    method: 'post',
    data: {
      paramSubId,
      goodsName,
      ylGoodsId,
      goodsSpecification,
      startTime,
      endTime
    }
  });
}

// 修改添加商品的，结束时间
export function editGoodsEndTime(id, endTime) {
  return request({
    url: '/dataCenter/api/v1/report/param/updateCategoryGoods',
    method: 'post',
    data: {
      id,
      endTime
    }
  });
}

// 获取商品类型下的商品列表
export function getGoodsListOfType(paramSubId, current, size) {
  return request({
    url: '/dataCenter/api/v1/report/param/listCategoryGoodsPage',
    method: 'post',
    data: {
      paramSubId,
      current,
      size
    }
  });
}

// 维护价格，价格列表
export function getParamsPriceList(
  paramId,
  current,
  size,
  goodsName,
  optName,
  startTime,
  endTime
) {
  return request({
    url: '/dataCenter/api/v1/report/param/listReportPricePage',
    method: 'get',
    params: {
      paramId,
      current,
      size,
      goodsName,
      optName, // 操作人
      startTime,
      endTime
    }
  });
}

// 添加商品价格
export function addParamsPrice(
  paramId,
  goodsId,
  goodsName,
  goodsSpecification,
  price,
  startTime,
  endTime,
  specificationId
) {
  return request({
    url: '/dataCenter/api/v1/report/param/addReportPrice',
    method: 'post',
    data: {
      paramId,
      goodsId,
      goodsName,
      goodsSpecification,
      price,
      startTime,
      endTime,
      specificationId
    }
  });
}
//  修改商品价格
export function editParamsPrice(id, endTime) {
  return request({
    url: '/dataCenter/api/v1/report/param/updateReportPrice',
    method: 'post',
    data: {
      id,
      endTime
    }
  });
}

// 阶梯下添加商品
export function saveLadderGoods(
  paramSubId,
  eid,
  goodsName,
  goodsSpecification,
  thresholdCount,
  ylGoodsId,
  rewardAmount,
  rewardPercentage,
  rewardType,
  startTime,
  endTime,
  goodsInSn,
  orderSource
) {
  return request({
    url: '/dataCenter/api/v1/report/param/saveLadderGoods',
    method: 'post',
    data: {
      paramSubId,
      eid,
      goodsName,
      goodsSpecification,
      thresholdCount,
      ylGoodsId,
      rewardAmount,
      rewardPercentage,
      rewardType,
      startTime,
      endTime,
      goodsInSn,
      orderSource
    }
  });
}

// 阶梯 活动 下查询商品列表
export function getQueryParamSubGoodsPageList(
  current,
  size,
  paramSubId,
  activityName,
  eid, // 商业名称
  userName,
  goodsName,
  startTime,
  endTime
) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryParamSubGoodsPageList',
    method: 'post',
    data: {
      current,
      size,
      paramSubId,
      activityName,
      eid, // 商业名称
      userName,
      goodsName,
      startTime,
      endTime
    }
  });
}
// 阶梯下 修改商品
export function updateLadderGoods(
  id,
  thresholdCount,
  rewardAmount,
  rewardPercentage,
  rewardType,
  startTime,
  endTime,
  orderSource
) {
  return request({
    url: '/dataCenter/api/v1/report/param/updateLadderGoods',
    method: 'post',
    data: {
      id,
      thresholdCount,
      rewardAmount,
      rewardPercentage,
      rewardType,
      startTime,
      endTime,
      orderSource
    }
  });
}
// 活动下添加 活动名称和商品
export function saveActivityGoods(
  paramSubId,
  activityName,
  eid,
  ylGoodsId,
  goodsName,
  goodsSpecification,
  rewardType,
  rewardAmount,
  rewardPercentage,
  startTime,
  endTime,
  goodsInSn,
  orderSource
) {
  return request({
    url: '/dataCenter/api/v1/report/param/saveActivityGoods',
    method: 'post',
    data: {
      paramSubId,
      activityName,
      eid,
      ylGoodsId,
      goodsName,
      goodsSpecification,
      rewardType,
      rewardAmount,
      rewardPercentage,
      startTime,
      endTime,
      goodsInSn,
      orderSource
    }
  });
}
// 修改促销活动下的商品
export function updateActivityGoods(
  id,
  rewardType,
  rewardAmount,
  rewardPercentage,
  startTime,
  endTime,
  activityName,
  orderSource
) {
  return request({
    url: '/dataCenter/api/v1/report/param/updateActivityGoods',
    method: 'post',
    data: {
      id,
      rewardType,
      rewardAmount,
      rewardPercentage,
      startTime,
      endTime,
      activityName,
      orderSource
    }
  });
}
// 维护参数 获取商家品与以岭品列表分页
export function getBussinessYLCorrespondingList(
  current,
  size,
  eid,
  opUserName,
  relationFlag,
  ylGoodsName,
  createTimeStart,
  createTimeEnd,
  // 商品名称
  goodsName,
  // 生产厂家
  goodsManufacturer,
  // 商品关系标签：1-以岭品 2-非以岭品 3-中药饮片 0-无标签 字典：flow_goods_relation_label
  goodsRelationLabelList
) {
  return request({
    url: '/dataCenter/api/v1/report/param/flowGoodsRelation/queryPageList',
    method: 'post',
    data: {
      current,
      size,
      eid,
      opUserName,
      relationFlag,
      ylGoodsName,
      createTimeStart,
      createTimeEnd,
      goodsName,
      goodsManufacturer,
      goodsRelationLabelList
    }
  });
}
// 维护参数 编辑商家品与以岭品
export function editBussinessYLCorrespondingList(
  id,
  ylGoodsId,
  ylGoodsName,
  ylGoodsSpecifications,
  // 商品关系标签
  goodsRelationLabel
) {
  return request({
    url: '/dataCenter/api/v1/report/param/flowGoodsRelation/edit',
    method: 'post',
    data: {
      id,
      ylGoodsId,
      ylGoodsName,
      ylGoodsSpecifications,
      goodsRelationLabel
    }
  });
}

// 获取订单返利报表
export function getDataTableList(
  current,
  size,
  eid,
  reportStatus,
  provinceCode,
  cityCode,
  regionCode,
  startReceiveTime,
  endReceiveTime,
  // 标识状态：0-全部 1-正常订单 2-无效订单 3-异常订单
  identificationStatus
) {
  return request({
    url: '/dataCenter/api/v1/report/order/queryB2bOrderRebateReportPage',
    method: 'get',
    params: {
      current,
      size,
      eid,
      reportStatus,
      provinceCode,
      cityCode,
      regionCode,
      startReceiveTime,
      endReceiveTime,
      identificationStatus
    }
  });
}

// 获取返利总表
export function getSummaryTableList(
  current,
  size,
  eid,
  provinceCode,
  cityCode,
  regionCode,
  startCreateTime,
  endCreateTime,
  // 返利类型 1-B2B返利 2-流向返利
  type,
  // 状态 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回
  status,
  // 订单返利状态：1-待返利 2-已返利 3-部分返利
  rebateStatus
) {
  return request({
    url: '/dataCenter/api/v1/report/queryReportPageList',
    method: 'get',
    params: {
      current,
      size,
      eid,
      provinceCode,
      cityCode,
      regionCode,
      startCreateTime,
      endCreateTime,
      type,
      status: status + '',
      rebateStatus
    }
  });
}
// 获取返利总表
export function updateSummaryTableAmount(reportId, adjustAmount, adjustReason) {
  return request({
    url: '/dataCenter/api/v1/report/adjust',
    method: 'post',
    data: {
      reportId,
      adjustAmount,
      adjustReason
    }
  });
}
// 获取返利总表  确认前判断b2b总表会员是否有退款
export function queryIsRefund(reportId) {
  return request({
    url: '/dataCenter/api/v1/report/queryIsRefund',
    method: 'get',
    params: {
      reportId
    }
  });
}

// 返利总表 确认
export function confirmSummaryTableAmount(reportId, type) {
  return request({
    url: '/dataCenter/api/v1/report/confirm',
    method: 'post',
    data: {
      reportId,
      type //确认类型 1-运营确认 2-财务确认
    }
  });
}
// 返利总表 驳回
export function rejectSummaryTableAmount(reportId, rejectType, rejectReason) {
  return request({
    url: '/dataCenter/api/v1/report/reject',
    method: 'post',
    data: {
      reportId,
      // 	驳回类型 1-运营驳回 2-财务驳回 3-管理员驳回
      rejectType,
      rejectReason
    }
  });
}
// 查询报表 ，流向返利报表
export function getQueryFlowOrderPageList(
  current,
  size,
  // 报表id(返利总表列表项的id)
  reportId,
  // 客户名称
  enterpriseName,
  // 标识状态：0-全部 1-正常订单,2-无效订单,3-异常订单
  identificationStatus,
  // 返利状态：0-全部 1-待返利 2-已返利 3-部分返利
  rebateStatus,
  startSoTime,
  endSoTime
) {
  return request({
    url: '/dataCenter/api/v1/report/queryFlowOrderPageList',
    method: 'get',
    params: {
      current,
      size,
      reportId,
      enterpriseName,
      identificationStatus,
      rebateStatus,
      startSoTime,
      endSoTime
    }
  });
}
// 查询报表 ，总表b2b订单明细
export function queryB2bPageList(
  current,
  size,
  // 报表id
  reportId,
  // 商业eid
  eid,
  // 标识状态：0-全部 1-正常订单,2-无效订单,3-异常订单
  identificationStatus,
  // 返利状态：0-全部 1-待返利 2-已返利 3-部分返利
  rebateStatus,
  // 省市区code
  provinceCode,
  cityCode,
  regionCode,
  // 开始下单时间
  startCreateOrderTime,
  // 结束下单时间
  endCreateOrderTime
) {
  return request({
    url: '/dataCenter/api/v1/report/queryB2bPageList',
    method: 'get',
    params: {
      current,
      size,
      reportId,
      eid,
      identificationStatus,
      rebateStatus,
      provinceCode,
      cityCode,
      regionCode,
      startCreateOrderTime,
      endCreateOrderTime
    }
  });
}
// 流向返利报表
export function getqueryFlowOrderRebateReportPage(
  current,
  size,
  eid,
  reportStatus,
  soSourceList,
  provinceCode,
  cityCode,
  regionCode,
  startSoTime,
  endSoTime,
  // 标识状态：1-正常订单,2-无效订单,3-异常订单
  identificationStatus
) {
  return request({
    url: '/dataCenter/api/v1/report/order/queryFlowOrderRebateReportPage',
    method: 'get',
    params: {
      current,
      size,
      eid,
      reportStatus,
      soSourceList: soSourceList + '',
      provinceCode,
      cityCode,
      regionCode,
      startSoTime,
      endSoTime,
      identificationStatus
    }
  });
}
// 流向返利报表  批量生成返利
export function calculateQueryFlowOrderRebateReportPage(
  current,
  size,
  eid,
  reportStatus,
  soSourceList,
  provinceCode,
  cityCode,
  regionCode,
  startSoTime,
  endSoTime,
  // 标识状态：1-正常订单,2-无效订单,3-异常订单
  identificationStatus
) {
  return request({
    url: '/dataCenter/api/v1/report/order/calculateFlowReward',
    method: 'post',
    data: {
      current,
      size,
      eid,
      reportStatus,
      soSourceList,
      provinceCode,
      cityCode,
      regionCode,
      endSoTime,
      startSoTime,
      identificationStatus
    }
  });
}
// 流向返利报表  批量生成返利
export function calculateQueryB2BOrderRebateReportPage(
  current,
  size,
  eid,
  reportStatus,
  provinceCode,
  cityCode,
  regionCode,
  startReceiveTime,
  endReceiveTime,
  // 标识状态：1-正常订单,2-无效订单,3-异常订单
  identificationStatus
) {
  return request({
    url: '/dataCenter/api/v1/report/order/calculateB2bReward',
    method: 'post',
    data: {
      current,
      size,
      eid,
      reportStatus,
      provinceCode,
      cityCode,
      regionCode,
      startReceiveTime,
      endReceiveTime,
      identificationStatus
    }
  });
}
// 报表参数-促销活动设置/阶梯规则设置-维护-查看(详情页)-删除
export function deleteParSubGoods(
  id
) {
  return request({
    url: '/dataCenter/api/v1/report/param/deleteParSubGoods',
    method: 'post',
    data: {
      id
    }
  });
}
// 订单返利表-供应商名称-搜索供应商列表
export function queryEnterpriseList(
  current,
  size,
  // 企业名称
  name
) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryEntInfoPageList',
    method: 'post',
    data: {
      current,
      size,
      // 企业名称
      name
    }
  });
}
// 报表参数-商销价-查询商品规格
export function queryGoodsSpecification(key) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryGoodsSpecification',
    method: 'get',
    params: {
      key
    }
  });
}
// 报表参数-商销价-根据规格id查询以岭品
export function queryYlGoods(sellSpecificationsId) {
  return request({
    url: '/dataCenter/api/v1/report/param/queryYlGoods',
    method: 'get',
    params: {
      sellSpecificationsId
    }
  })
}
// 返利总表-B2B类型详情页-标识返利(修改)
export function requestChangeRebate(
  reportId,
  detailIdList
) {
  return request({
    url: '/dataCenter/api/v1/report/rebate',
    method: 'post',
    data: {
      reportId,
      detailIdList
    }
  });
}
// 数据报表-B2B返利订单报表-标识返利(按搜索结果全部修改)
export function updateB2bIdentification(
  // 商业eid
  eid,
  // 开始签收时间
  startReceiveTime,
  // 结束签收时间
  endReceiveTime,
  // 省市区
  provinceCode,
  cityCode,
  regionCode,
  // 操作状态-1-全部 0-待返利 4-运营驳回 5-财务驳回 6-管理员驳回
  reportStatus,
  // 标识状态：1-正常订单,2-无效订单,3-异常订单
  identificationStatus,
  // 更新修改的标识状态：1-正常订单,2-无效订单,3-异常订单
  updateIdenStatus,
  // 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
  abnormalReason,
  // 异常描述
  abnormalDescribed
) {
  return request({
    url: '/dataCenter/api/v1/report/order/updateB2bIdentification',
    method: 'post',
    data: {
      eid,
      startReceiveTime,
      endReceiveTime,
      provinceCode,
      cityCode,
      regionCode,
      reportStatus,
      identificationStatus,
      updateIdenStatus,
      abnormalReason,
      abnormalDescribed
    }
  });
}
// 数据报表-B2B返利订单报表-标识返利(按勾选部分修改)
export function updateProportionIdentification(
  // 类型：1-B2B返利 2-流向返利
  type,
  // 勾选的id集合
  idList,
  // 要修改的标识状态：1-正常订单,2-无效订单,3-异常订单
  updateIdenStatus,
  // 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
  abnormalReason,
  // 异常描述
  abnormalDescribed
) {
  return request({
    url: '/dataCenter/api/v1/report/order/updateProportionIdentification',
    method: 'post',
    data: {
      type,
      idList,
      updateIdenStatus,
      abnormalReason,
      abnormalDescribed
    }
  });
}
// 数据报表-流向返利报表-标识返利(按搜索结果全部修改)
export function updateFlowIdentification(
  // 商业eid
  eid,
  // 	开始下单时间
  startSoTime,
  // 	结束下单时间
  endSoTime,
  // 省市区
  provinceCode,
  cityCode,
  regionCode,
  // 操作状态-1-全部 0-待返利 4-运营驳回 5-财务驳回 6-管理员驳回
  reportStatus,
  // 标识状态：1-正常订单,2-无效订单,3-异常订单
  identificationStatus,
  // 更新修改的标识状态：1-正常订单,2-无效订单,3-异常订单
  updateIdenStatus,
  // 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
  abnormalReason,
  // 异常描述
  abnormalDescribed
) {
  return request({
    url: '/dataCenter/api/v1/report/order/updateFlowIdentification',
    method: 'post',
    data: {
      eid,
      startSoTime,
      endSoTime,
      provinceCode,
      cityCode,
      regionCode,
      reportStatus,
      identificationStatus,
      updateIdenStatus,
      abnormalReason,
      abnormalDescribed
    }
  });
}

// 数据报表-进销存中心
export function queryPurchasePageList(
  current,
  size,
  eid,
  // 以岭品id
  ylGoodsId
) {
  return request({
    url: '/dataCenter/api/v1/report/stock/queryPurchasePage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ylGoodsId
    }
  });
}

//数据报表-进销存中心-调整库存
export function reviseStockList(
  //库存id
  id,
  //调整库存
  quantity
) {
  return request({
    url: '/dataCenter/api/v1/report/stock/reviseStock',
    method: 'post',
    data: {
      id,
      quantity
    }
  });
}

//数据报表-进销存中心-查询库存调整记录
export function queryStockRevisePageList(
  current,
  size,
  //库存id
  id
) {
  return request({
    url: '/dataCenter/api/v1/report/stock/queryStockRevisePage',
    method: 'post',
    data: {
      current,
      size,
      id
    }
  });
}

//数据报表-进销存中心-查询库存占用记录(已返利库存)
export function queryStockOccupyPageList(
  current,
  size,
  eid,
  //商品内码
  goodsInSn,
  //采购渠道：1-大运河采购 2-京东采购
  purchaseChannel,
  //以岭品id
  ylGoodsId
) {
  return request({
    url: '/dataCenter/api/v1/report/stock/queryStockOccupyPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      goodsInSn,
      purchaseChannel,
      ylGoodsId
    }
  });
}

// 以岭品名称查询列表
export function queryGoodsList(key) {
  return request({
    url: '/dataCenter/api/v1/report/stock/queryGoods',
    method: 'get',
    params: {
      key
    }
  });
}

// 数据报表-报表参数-会员类型-会员名称
export function queryMemberList() {
  return request({
    url: '/dataCenter/api/v1/report/param/getMemberList',
    method: 'get',
    params: {
    }
  });
}