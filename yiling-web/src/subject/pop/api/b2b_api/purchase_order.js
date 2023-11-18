import request from '@/subject/pop/utils/request'

// 采购订单列表
export function getPurchaseList(
  current,
  size,
  // 企业名称
  name,
  // 订单号
  orderNo,
  // 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
  orderStatus,
  // 支付方式：1-线下支付 2-账期 3-预付款
  paymentMethod,
  // 支付状态：1-待支付 2-已支付
  paymentStatus,
  startCreateTime,
  endCreateTime
) {
  return request({
    url: '/admin/b2b/api/v1/order/purchase/list',
    method: 'post',
    data: {
      current,
      size,
      name,
      orderNo,
      orderStatus,
      paymentMethod,
      paymentStatus,
      startCreateTime,
      endCreateTime
    }
  })
}

// 采购取消订单
export function purchaseCancle(orderId) {
  return request({
    url: '/admin/b2b/api/v1/order/purchase/cancel',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 采购删除订单
export function purchaseDelete(orderId) {
  return request({
    url: '/admin/b2b/api/v1/order/purchase/hide',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 去支付
export function toPay(tradeType, list) {
  return request({
    url: '/payment/api/v1/payment/toPay',
    method: 'post',
    data: {
      tradeType,
      list
    }
  })
}

// 在线支付择支付方式
export function paymentPay(
  payId, 
  paySource,
  redirectUrl,
  tradeSource
  ) {
  return request({
    url: '/payment/api/v1/payment/pay',
    method: 'post',
    data: {
      payId, 
      paySource,
      redirectUrl,
      tradeSource
    }
  })
}

// 查询订单是否支付成功
export function paymentQuery(payNo) {
  return request({
    url: '/payment/api/v1/payment/query',
    method: 'get',
    params: {
      payNo
    }
  })
}

// 采购订单详情
export function getPurchaseDetail(orderId) {
  return request({
    url: '/admin/b2b/api/v1/order/purchase/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 采购订单收货
export function purchaseReceive(orderId) {
  return request({
    url: '/admin/b2b/api/v1/order/purchase/receive',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 采购退货单申请
export function returnApply(
  orderId, 
  orderNo,
  orderReturnDetailList,
  remark
  ) {
  return request({
    url: '/admin/b2b/api/v1/return/apply',
    method: 'post',
    data: {
      orderId, 
      orderNo,
      orderReturnDetailList,
      remark
    }
  })
}

// 采购退货单列表
export function purchaseOrderReturnPageList(
  current,
  size,
  sellerEname,
  // 订单号
  orderNo,
  returnType,
  returnStatus,
  startTime,
  endTime
) {
  return request({
    url: '/admin/b2b/api/v1/return/purchaseOrderReturnPageList',
    method: 'post',
    data: {
      current,
      size,
      sellerEname,
      orderNo,
      returnType,
      returnStatus,
      startTime,
      endTime
    }
  })
}

// 退货单详情
export function sellerOrderReturnDetail(returnOrderId) {
  return request({
    url: '/admin/b2b/api/v1/return/sellerOrderReturnDetail',
    method: 'get',
    params: {
      returnOrderId
    }
  })
}
