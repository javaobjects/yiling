import request from '@/subject/pop/utils/request'

// 获取采购订单列表
export function orderEnterpriseList(
  current,
  size,
  // 商业名称
  name,
  // 订单号
  orderNo,
  // 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
  orderStatus,
  // 支付状态：1-待支付 2-已支付
  paymentStatus,
  // 支付方式：1-线下支付 2-账期 3-预付款
  paymentMethod,
  // 订单类型：1-POP订单,2-B2B订单
  orderType,
  // 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
  orderSource,
  //省
  provinceCode,
  //市
  cityCode,
  //区
  regionCode,
  // 订单类型：1-销售订单 2-采购订单
  type,
  startCreateTime,
  endCreateTime
) {
  return request({
    url: '/admin/pop/api/v1/order/enterprise/list',
    method: 'post',
    data: {
      current,
      size,
      name,
      orderNo,
      orderStatus,
      paymentStatus,
      paymentMethod,
      orderType,
      orderSource,
      provinceCode,
      cityCode,
      regionCode,
      type,
      startCreateTime,
      endCreateTime
    }
  })
}

// 获取采购订单详情
export function getOrderDetail(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/enterprise/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 获取退货订单详情
export function refundEnterpriseDetail(returnOrderId) {
  return request({
    url: '/admin/pop/api/v1/refund/enterprise/detail',
    method: 'get',
    params: {
      returnOrderId
    }
  })
}

// 获取退货订单列表
export function refundEnterpriseList(
  current,
  size,
  // 企业名称
  name,
  // 订单号
  orderNo,
  // 退货单号
  orderReturnNo,
  // 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
  returnType,
  // 订单状态 1-待审核 2-审核通过 3-审核驳回
  returnStatus,
  // 订单类型：1-销售订单 2-
  type,
  // 开始时间
  startTime,
  // 结束时间
  endTime
) {
  return request({
    url: '/admin/pop/api/v1/refund/enterprise/list',
    method: 'post',
    data: {
      current,
      size,
      name,
      orderNo,
      orderReturnNo,
      returnType,
      returnStatus,
      type,
      startTime,
      endTime
    }
  })
}

