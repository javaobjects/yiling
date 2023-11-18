import request from '@/subject/pop/utils/request'

// 获取订单列表
export function getOrderList(
  current,
  size,
  // 订单类型：1-销售订单 2-采购订单
  type,
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
  endCreateTime,
  // 省
  provinceCode,
  // 市
  cityCode,
  // 区
  regionCode,
  stateType
) {
  return request({
    url: '/admin/b2b/api/v1/order/list',
    method: 'post',
    data: {
      current,
      name,
      type,
      size,
      orderNo,
      orderStatus,
      paymentMethod,
      paymentStatus,
      startCreateTime,
      endCreateTime,
      provinceCode,
      cityCode,
      regionCode,
      stateType
    }
  })
}

// 获取销售订单数目
export function getSaleOrderNums() {
  return request({
    url: '/admin/b2b/api/v1/order/get/number',
    method: 'get',
    params: {}
  })
}

// 销售订单列表取消订单
export function orderCancle(orderId) {
  return request({
    url: '/admin/b2b/api/v1/order/cancel',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 确认回款
export function orderConfirmPayment(orderId) {
  return request({
    url: '/admin/b2b/api/v1/order/confirm/payment',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 销售订单列表-修改物流信息
export function updateTransport(
  orderId,
  deliveryType,
  deliveryCompany,
  deliveryNo
) {
  return request({
    url: '/admin/b2b/api/v1/order/update/transport',
    method: 'post',
    data: {
      orderId,
      deliveryType,
      deliveryCompany,
      deliveryNo
    }
  })
}

// 销售订单详情
export function getSellOrderDetail(orderId) {
  return request({
    url: '/admin/b2b/api/v1/order/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 销售订单发货
export function orderDelivery(
  // 订单id
  orderId,
  // 物流类型：1-自有物流 2-第三方物流
  deliveryType,
  // 商品信息
  orderDeliveryGoodsInfoList,
  // 物流公司
  deliveryCompany,
  // 物流单号
  deliveryNo
) {
  return request({
    url: '/admin/b2b/api/v1/order/delivery',
    method: 'post',
    data: {
      orderId,
      deliveryType,
      orderDeliveryGoodsInfoList,
      deliveryCompany,
      deliveryNo
    }
  })
}

// 审核订单通过
export function orderReviewResole(id) {
  return request({
    url: '/admin/pop/api/v1/order/manage/review/pass',
    method: 'get',
    params: {
      id
    }
  })
}

// 获取审核订单数量
export function getOrderReviewNum() {
  return request({
    url: '/admin/pop/api/v1/order/manage/get/number',
    method: 'get',
    params: {}
  })
}

// ----------销售订单 退货单--------------

// 销售退货单数量接口
export function getSellerOrderNums() {
  return request({
    url: '/admin/b2b/api/v1/return/get/number/seller',
    method: 'get',
    params: {
    }
  })
}

// 销售退货单列表
export function getReturnGoodsList(
  current,
  size,
  // 订单号
  orderNo,
  // 退货单号
  orderReturnNo,
  // 订单状态 1-待审核 2-审核通过 3-审核驳回
  returnStatus,
  // 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
  returnType,
  // 采购商名称
  buyerEname,
  // 开始时间
  startTime,
  // 结束时间
  endTime,
  // 来自哪里 1-财务，2-商务
  fromWhere
) {
  return request({
    url: '/admin/b2b/api/v1/return/sellerOrderReturnPageList',
    method: 'post',
    data: {
      current,
      size,
      orderNo,
      orderReturnNo,
      returnStatus,
      returnType,
      buyerEname,
      startTime,
      endTime,
      fromWhere
    }
  })
}

// 销售退货单详情
export function getSellerOrderReturnDetail(
  returnOrderId
) {
  return request({
    url: '/admin/b2b/api/v1/return/sellerOrderReturnDetail',
    method: 'get',
    params: {
      returnOrderId
    }
  })
}

// 退货单申请驳回
export function returnOrderReject(
  returnId,
  // 审核是否通过 bool
  isSuccess,
  // 驳回原因
  failReason
) {
  return request({
    url: '/admin/b2b/api/v1/return/verify',
    method: 'post',
    data: {
      returnId,
      failReason,
      isSuccess: isSuccess ? 0 : 1
    }
  })
}
