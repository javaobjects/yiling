import request from '@/subject/admin/utils/request'

// 获取采购订单列表
export function getOrderList(
  current,
  size,
  // 买家名称
  buyerEname,
  // 卖家名称
  sellerEname,
  // 订单号
  orderNo,
  // 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
  orderStatus,
  // 支付方式：1-线下支付 2-账期 3-预付款
  paymentMethod,
  // 支付状态：1-待支付 2-已支付
  paymentStatus,
  startCreateTime,
  endCreateTime,
  startDeliveryTime,
  endDeliveryTime,
  // 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
  orderSource,
  //省
  provinceCode,
  //市
  cityCode,
  //区
  regionCode,
  // 活动方式: 2-特价 3-秒杀
  activityType,
  // 订单类型：1-POP订单,2-B2B订单
  orderType
) {
  return request({
    url: '/dataCenter/api/v1/order/get/page',
    method: 'post',
    data: {
      current,
      size,
      buyerEname,
      sellerEname,
      orderNo,
      orderStatus,
      paymentMethod,
      paymentStatus,
      startCreateTime,
      endCreateTime,
      startDeliveryTime,
      endDeliveryTime,
      provinceCode,
      cityCode,
      regionCode,
      orderSource,
      activityType,
      orderType
    }
  })
}

// 获取采购订单详情
export function getOrderDetail(orderId) {
  return request({
    url: '/dataCenter/api/v1/order/get/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 查看发票信息
export function checkOrderTicket(
  // 订单id
  orderId
) {
  return request({
    url: '/pop/api/v1/order/invoice/get/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 获取退货订单数量接口
export function getGoodsReturnedNumber(returnOrderId) {
  return request({
    url: '/dataCenter/api/v1/refund/get/number',
    method: 'get',
    params: {
      returnOrderId
    }
  })
}

// 获取退货订单详情
export function getRejectOrderDetail(returnOrderId) {
  return request({
    url: '/dataCenter/api/v1/refund/operatorOrderReturnDetail',
    method: 'get',
    params: {
      returnOrderId
    }
  })
}

// 获取退货订单列表
export function getRejectOrderList(
  current,
  size,
  // 订单号
  orderNo,
  // 采购商名称
  buyerEname,
  // 订单状态 1-待审核 2-审核通过 3-审核驳回
  returnStatus,
  // 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
  returnType,
  // 供应商名称
  sellerEname,
  // 开始时间
  startTime,
  // 结束时间
  endTime,
  // 数据来源
  returnSource,
  // 退货单号
  orderReturnNo
) {
  return request({
    url: '/dataCenter/api/v1/refund/operatorOrderReturnPageList',
    method: 'post',
    data: {
      current,
      size,
      orderNo,
      buyerEname,
      returnStatus,
      returnType,
      sellerEname,
      startTime,
      endTime,
      returnSource,
      orderReturnNo
    }
  })
}

