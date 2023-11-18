import request from '@/subject/pop/utils/request'

// 设置店铺
export function setShop(
  // 店铺logo
  shopLogo,
  // 店铺简介
  shopDesc,
  // 店铺区域编码
  areaJsonString,
  // 起配金额
  startAmount,
  // 支付方式
  paymentMethodList
) {
  return request({
    url: '/admin/b2b/api/v1/shop/setShop',
    method: 'post',
    data: {
      shopLogo,
      shopDesc,
      areaJsonString,
      startAmount,
      paymentMethodList
    }
  })
}

// 获取店铺设置详情
export function getShop(
) {
  return request({
    url: '/admin/b2b/api/v1/shop/getShop',
    method: 'get',
    params: {
    }
  })
}

// 获取店铺公告
export function getShopAnnounce(
) {
  return request({
    url: '/admin/b2b/api/v1/shop/getShopAnnouncement',
    method: 'get',
    params: {
    }
  })
}

// 设置店铺公告
export function setShopAnnounce(
  // 店铺公告
  shopAnnouncement
) {
  return request({
    url: '/admin/b2b/api/v1/shop/updateShopAnnouncement',
    method: 'post',
    data: {
      shopAnnouncement
    }
  })
}
