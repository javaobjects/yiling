//财务管理
import request from '@/subject/admin/utils/request'

// 查询订单明细列表
export function queryOrderDetailPageList(
  current, //第几页，默认：1
	orderId, //订单id
	settlementId, //结算单id
	size //每页记录数，默认：10
){
  return request({
    url: '/b2b/api/v1/settlement/queryOrderDetailPageList',
    method: 'post',
    data: {
      current,
      orderId,
      settlementId,
      size
    }
  })
}
// 打款
export function payment(
  settlementIds = [], //结算单id
  settlementRemark // 备注
){
  return request({
    url: '/b2b/api/v1/settlement/payment',
    method: 'post',
    data: {
      settlementIds,
      settlementRemark
    }
  })
}
// 查询结算单明细列表
export function querySettlementDetailPageList(
  current, //第几页，默认：1
	settlementId, //结算单id
	size //每页记录数，默认：10
){
  return request({
    url: '/b2b/api/v1/settlement/querySettlementDetailPageList',
    method: 'post',
    data: {
      current,
      settlementId,
      size
    }
  })
}
// 订单对账---查询订单明细列表
export function querySettlementOrderDetailPageList(
  current, //第几页，默认：1
	orderId, //订单id
	size //每页记录数，默认：10
){
  return request({
    url: '/b2b/api/v1/settlement/querySettlementOrderDetailPageList',
    method: 'post',
    data: {
      current,
      orderId,
      size
    }
  })
}
// 查询订单对账列表
export function querySettlementOrderPageList(
  //货款结算状态
  goodsStatus, 
  //最大创建时间
	maxCreateTime,
  //最小创建时间
	minCreateTime,
  //订单号
	orderNo,
  //促销结算状态 
	saleStatus,
  // 预售违约结算状态
  presaleDefaultStatus,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/b2b/api/v1/settlement/querySettlementOrderPageList',
    method: 'post',
    data: {
      goodsStatus, 
      maxCreateTime, 
      minCreateTime,
      orderNo, 
      saleStatus,
      presaleDefaultStatus,
      current,
      size
    }
  })
}
// 查询结算单列表
export function querySettlementPageList(
  code, //结算单号
	current, //	第几页，默认：1
  entName, //供应商名称
	maxTime, //开始生成结算单最大时间
	minTime, //开始生成结算单最小时间
	size, //每页记录数，默认：10
	status, //结算单状态
	type //	结算单类型
){
  return request({
    url: '/b2b/api/v1/settlement/querySettlementPageList',
    method: 'post',
    data: {
      code, 
      current,
      entName,
      maxTime,
      minTime,
      size,
      status,
      type
    }
  })
}
// 退款单
export function tkdPageList(
  buyerName, //	采购商名称
	createStartTime, //创建开始时间
	createStopTime, //创建截止时间
	current, //第几页，默认：1
	orderNo, //订单编号
	refundStatus, //退款状态1-待退款 2-退款中 3-退款成功 4-退款失败
	refundType, //退款类型1-订单取消退款 2-采购退货退款 3-商家驳回
	sellerName, //供应商名称
	size //每页记录数，默认：10
){
  return request({
    url: '/b2b/api/v1/refund/pageList',
    method: 'post',
    data: {
      buyerName,
      createStartTime,
      createStopTime,
      current,
      orderNo,
      refundStatus,
      refundType,
      sellerName,
      size
    }
  }) 
}
// 财务管理-退款单-财务管理
export function tkdRetry(
  operate, //1-已退款，仅标记已处理 2-未退款，通过接口退款
  refundId //退款单id
){
  return request({
    url: '/b2b/api/v1/refund/retry',
    method: 'post',
    data: {
      operate,
      refundId
    }
  })
}
// 重复支付
// 财务管理-重复退款单列表
export function repeat(
  appOrderNo, //订单号
	current, //第几页，默认：1
	endPayTime, //	支付结束时间
	payWay, //支付方式
	dealState, //	处理状态 
	size, //每页记录数，默认：10
	startPayTime, //支付开始时间
	tradeType //单据类型(1:其他,2:支付,3:在线还款,4:转账,5:会员)	
){
  return request({
    url: '/b2b/api/v1/refund/repeat',
    method: 'post',
    data: {
      appOrderNo,
      current,
      endPayTime,
      payWay,
      dealState,
      size,
      startPayTime,
      tradeType
    }
  })
}
// 重复退款-财务管理
export function process(
  methodType, //退款处理方式：1，未退款通过接口退款，2，已退款标记已处理
	repeatOrderId //重复退款Id
){
  return request({
    url: '/b2b/api/v1/refund/repeat/process',
    method: 'post',
    data: {
      methodType,
      repeatOrderId
    }
  })
}

// 会员回款记录-查询结算单列表
export function yeeSettleQuerySettlementPageList(
  current,
  size,
  // 结算单号
  summaryNo,
  createTimeBegin,
  createTimeEnd
  ) {
  return request({
    url: '/b2b/api/v1/yeeSettle/querySettlementPageList',
    method: 'post',
    data: {
      current,
      size,
      summaryNo,
      createTimeBegin,
      createTimeEnd
    }
  })
}