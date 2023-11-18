import request from '@/subject/admin/utils/request'

// 分页列表营销活动预售
export function presaleActivityPageList(
  current,
  size,
  // 活动名称
  name,
  // 活动状态：1-启用 2-停用
  status,
  // 活动进度：1-未开始 2-进行中 3-已结束
  progress,
  // 创建人名称
  createUserName,
  // 创建人手机号
  createTel,
  startTime,
  stopTime,
  // 活动分类：1-平台活动 2-商家活动
  sponsorType,
  // 运营备注
  operatingRemark
) {
  return request({
    url: '/b2b/api/v1/presale/activity/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      status,
      progress,
      createUserName,
      createTel,
      startTime,
      stopTime,
      sponsorType,
      operatingRemark
    }
  })
}

// 分页列表预售活动关联的订单信息
export function pageListForPresaleOrderInfo(
  current,
  size,
  // 预售活动id
  id
) {
  return request({
    url: '/b2b/api/v1/presale/activity/pageListForPresaleOrderInfo',
    method: 'post',
    data: {
      current,
      size,
      id
    }
  })
}

// 预售主信息--停用
export function presaleActivityUpdateStatus(
  // 预售活动id
  id
) {
  return request({
    url: '/b2b/api/v1/presale/activity/updateStatus',
    method: 'post',
    data: {
      id
    }
  })
}

// 预售主信息--复制
export function presaleActivityCopy(
  // 预售活动id
  id
) {
  return request({
    url: '/b2b/api/v1/presale/activity/copy',
    method: 'post',
    data: {
      id
    }
  })
}

// 商品预售-选择需添加客户列表查询-运营后台
export function strategyLimitBuyerPage(
  current,
  size,
  // 企业ID
  id,
  // 企业名称
  name
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/buyer/page',
    method: 'post',
    data: {
      current,
      size,
      id,
      name
    }
  })
}

// 商品预售-已添加客户列表查询-运营后台
export function presaleBuyerLimitBuyerPageList(
  current,
  size,
  marketingStrategyId,
  // 企业ID
  eid,
  // 企业名称
  ename
  ) {
  return request({
    url: '/b2b/api/v1/presaleBuyer/limit/buyer/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      eid,
      ename
    }
  })
}

// 商品预售-添加客户-运营后台
export function presaleBuyerLimitBuyerAdd(
  // 营销活动id
  marketingPresaleId,
  // 企业id集合
  eidList,
  // 企业ID
  eidPage,
  // 企业名称
  enamePage
  ) {
  return request({
    url: '/b2b/api/v1/presaleBuyer/limit/buyer/add',
    method: 'post',
    data: {
      marketingPresaleId,
      eidList,
      eidPage,
      enamePage
    }
  })
}

// 商品预售-删除客户-运营后台
export function presaleBuyerLimitBuyerDelete(
  // 营销活动id
  marketingPresaleId,
  // 企业id集合
  eidList,
  // 企业ID
  eidPage,
  // 企业名称
  enamePage
  ) {
  return request({
    url: '/b2b/api/v1/presaleBuyer/limit/buyer/delete',
    method: 'post',
    data: {
      marketingPresaleId,
      eidList,
      eidPage,
      enamePage
    }
  })
}

// 预售活动选择会员方案-选择需添加的会员方案-运营后台
export function strategyLimitMemberPage(
  current,
  size
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/member/page',
    method: 'post',
    data: {
      current,
      size
    }
  })
}

// 预售活动选择会员方案-已添加会员方案列表查询-运营后台
export function presaleMemberLimitMemberPageList(
  current,
  size,
  marketingPresaleId
  ) {
  return request({
    url: '/b2b/api/v1/presaleMember/limit/member/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingPresaleId
    }
  })
}

// 预售活动选择会员方案-添加会员方案-运营后台
export function presaleMemberLimitMemberAdd(
  marketingPresaleId,
  memberId
  ) {
  return request({
    url: '/b2b/api/v1/presaleMember/limit/member/add',
    method: 'post',
    data: {
      marketingPresaleId,
      memberId
    }
  })
}

// 预售活动选择会员方案-删除会员方案-运营后台
export function presaleMemberLimitMemberDelete(id) {
  return request({
    url: '/b2b/api/v1/presaleMember/limit/member/delete',
    method: 'post',
    data: {
      id
    }
  })
}

// 预售活动指定推广方会员-已添加推广方会员商家列表查询-运营后台
export function presalePromoterLimitPromoterMemberPageList(
  current,
  size,
  // 营销活动id
  marketingPresaleId,
  eid,
  ename,
  // 所属省份编码
  provinceCode,
  // 所属城市编码
  cityCode,
  // 所属区域编码
  regionCode
  ) {
  return request({
    url: '/b2b/api/v1/presalePromoter/limit/promoter/member/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingPresaleId,
      eid,
      ename,
      provinceCode,
      cityCode,
      regionCode
    }
  })
}

// 预售活动指定推广方会员商家-添加推广方会员商家-运营后台
export function presalePromoterLimitPromoterMemberAdd(
  marketingPresaleId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/presalePromoter/limit/promoter/member/add',
    method: 'post',
    data: {
      marketingPresaleId,
      eidList
    }
  })
}

// 预售活动指定推广方会员商家-删除商家-运营后台
export function presalePromoterLimitPromoterMemberDelete(
  marketingPresaleId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/presalePromoter/limit/promoter/member/delete',
    method: 'post',
    data: {
      marketingPresaleId,
      eidList
    }
  })
}

// 预售主信息保存--上面的保存按钮
export function presaleActivitySave(addParams) {
  return request({
    url: '/b2b/api/v1/presale/activity/save',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 预售主信息保存--具体内容
export function presaleActivitySaveAll(addParams) {
  return request({
    url: '/b2b/api/v1/presale/activity/saveAll',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 查询预售详情-运营后台
export function presaleActivityQueryDetail(id) {
  return request({
    url: '/b2b/api/v1/presale/activity/queryDetail',
    method: 'get',
    params: {
      id
    }
  })
}