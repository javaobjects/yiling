import request from '@/subject/admin/utils/request'

// 优惠券活动列表
export function getGiftList(
  // 优惠券名称
  name,
  // 优惠券ID
  id,
  // 所属业务
  businessType,
  // 商品类别 【1-真实， 2-虚拟物品】
  goodsType
) {
  return request({
    url: '/b2b/api/v1/goods/gift/get/list',
    method: 'post',
    data: {
      name,
      id,
      businessType,
      goodsType
    }
  })
}

// 保存赠品
export function giftSave(
  addParams
) {
  return request({
    url: '/b2b/api/v1/goods/gift/save',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 修改赠品
export function giftUpdate(
  addParams
) {
  return request({
    url: '/b2b/api/v1/goods/gift/update',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 赠品库明细
export function getGiftDetail(
  id
) {
  return request({
    url: '/b2b/api/v1/goods/gift/get',
    method: 'get',
    params: {
      id
    }
  })
}

// 赠品库删除
export function giftDelete(
  id
) {
  return request({
    url: '/b2b/api/v1/goods/gift/delete',
    method: 'get',
    params: {
      id
    }
  })
}

// 获取优惠券列表
export function queryListPageForGoodsGift(
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/couponActivity/queryListPageForGoodsGift',
    method: 'post',
    data: {
      current,
      size
    }
  })
}
