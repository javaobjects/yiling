import request from '@/subject/pop/utils/request'

// 分页列表查询
export function promotionActivityPageList(
  current,
  size,
  // 活动名称
  name,
  // 活动类型
  type,
  // 活动状态
  status,
  // 活动创建人
  createUserName,
  // 创建人手机号
  createUserTel,
  beginTime,
  endTime,
  remark
) {
  return request({
    url: '/admin/b2b/api/v1/promotion/activity/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      type,
      status,
      createUserName,
      createUserTel,
      beginTime,
      endTime,
      remark
    }
  })
}

// 查询参与满赠活动的订单信息
export function pageGiftOrder(
  current,
  size,
  // 活动id
  promotionActivityId
) {
  return request({
    url: '/admin/b2b/api/v1/promotion/activity/pageGiftOrder',
    method: 'post',
    data: {
      current,
      size,
      promotionActivityId
    }
  })
}

// b2b后台商品弹框查询
export function goodsB2bList(
  current,
  size,
  from,
  goodsStatus,
  eidList,
  promotionActivityId,
  goodsId,
  goodsName,
  yilingGoodsFlag,
  isAvailableQty
) {
  return request({
    url: '/admin/b2b/api/v1/goods/b2bList',
    method: 'post',
    data: {
      current,
      size,
      from,
      goodsStatus,
      eidList,
      promotionActivityId,
      goodsId,
      goodsName,
      yilingGoodsFlag,
      isAvailableQty
    }
  })
}

// 保存按钮
export function promotionActivitySave(addParams) {
  return request({
    url: '/admin/b2b/api/v1/promotion/activity/save',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 保存按钮
export function promotionActivitySubmit(addParams) {
  return request({
    url: '/admin/b2b/api/v1/promotion/activity/submit',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 状态修改-运营后台
export function promotionActivityStatus(id, status) {
  return request({
    url: '/admin/b2b/api/v1/promotion/activity/status',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 复制-商家后台
export function promotionActivityCopy(id, status) {
  return request({
    url: '/admin/b2b/api/v1/promotion/activity/copy',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 编辑和查询详情-商家后台
export function promotionActivityQueryById(id) {
  return request({
    url: '/admin/b2b/api/v1/promotion/activity/queryById',
    method: 'get',
    params: {
      id
    }
  })
}
