import request from '@/subject/admin/utils/request'

// 统计报表-B2B订单报表-商品标签
export function requestGoodsTags(current, size, name) {
  return request({
    url: '/dataCenter/api/v1/report/order/get/goods/lag',
    method: 'post',
    data: {
      current,
      size,
      name
    }
  })
}
// 统计报表-B2B订单报表-B2B订单报表
export function requestB2BCount(
  // 	供应商EID
  sellerEid,
  // 开始结束时间
  startTime,
  endTime,
  // 省市区
  provinceCode,
  cityCode,
  regionCode,
  // 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
  paymentMethod,
  // 商品id标签
  standardGoodsTagId,
  // 下单时是否会员 1-非会员 2-是会员
  vipFlag,
  // 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
  orderSource
) {
  return request({
    url: '/dataCenter/api/v1/report/order/B2B/count',
    method: 'post',
    data: {
      sellerEid,
      startTime,
      endTime,
      provinceCode,
      cityCode,
      regionCode,
      paymentMethod,
      standardGoodsTagId,
      vipFlag,
      orderSource
    }
  })
}
