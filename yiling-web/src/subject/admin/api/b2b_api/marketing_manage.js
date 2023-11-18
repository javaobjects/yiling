import request from '@/subject/admin/utils/request'

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
  bear,
  beginTime,
  endTime,
  remark
) {
  return request({
    url: '/b2b/api/v1/promotion/activity/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      type,
      status,
      createUserName,
      createUserTel,
      bear,
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
    url: '/b2b/api/v1/promotion/activity/pageGiftOrder',
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
  type,
  yilingGoodsFlag,
  sponsorType,
  from,
  goodsStatus,
  eidList,
  promotionActivityId,
  goodsId,
  goodsName,
  isAvailableQty // 可用库存是否大于零 0全部库存 1可用库存大于0
) {
  return request({
    url: '/b2b/api/v1/b2b/goods/b2bList',
    method: 'post',
    data: {
      current,
      size,
      type,
      yilingGoodsFlag,
      sponsorType,
      from,
      goodsStatus,
      eidList,
      promotionActivityId,
      goodsId,
      goodsName,
      isAvailableQty
    }
  })
}

// 保存按钮
export function promotionActivitySave(addParams) {
  return request({
    url: '/b2b/api/v1/promotion/activity/save',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 保存按钮
export function promotionActivitySubmit(addParams) {
  return request({
    url: '/b2b/api/v1/promotion/activity/submit',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 状态修改-运营后台
export function promotionActivityStatus(id, status) {
  return request({
    url: '/b2b/api/v1/promotion/activity/status',
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
    url: '/b2b/api/v1/promotion/activity/copy',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 状态修改-运营后台
export function promotionActivityQueryById(id) {
  return request({
    url: '/b2b/api/v1/promotion/activity/queryById',
    method: 'get',
    params: {
      id
    }
  })
}

// 秒杀/特价 查询供应商
export function activityQueryEnterpriseListPage(current, size, merchantType, eid, ename) {
  return request({
    url: '/b2b/api/v1/promotion/activity/queryEnterpriseListPage',
    method: 'post',
    data: {
      current,
      size,
      merchantType,
      eid,
      ename
    }
  })
}

// ******************** 专场活动 ********************
// 专场活动 - 分页列表查询
export function specialActivityPageList(
  current,
  size,
  // 活动名称
  specialActivityName,
  // 活动状态
  status,
  // 活动类型
  type,
  // 专场活动开始时间-起始时间
  beginStartTime,
  // 专场活动开始时间-截止时间
  beginEndTime,
  // 专场活动结束时间-起始时间
  startTime,
  // 专场活动结束时间-截止时间
  endTime
) {
  return request({
    url: '/b2b/api/v1/promotion/special/activity/pageList',
    method: 'post',
    data: {
      current,
      size,
      specialActivityName,
      status,
      type,
      beginStartTime,
      beginEndTime,
      startTime,
      endTime
    }
  })
}

// 专场活动 - 查询专场活动中关联的营销活动信息
export function specialPageActivityInfo(
  current,
  size,
  // 企业名称
  ename,
  // 活动状态
  status,
  // 活动类型
  type,
  startTime,
  endTime
) {
  return request({
    url: '/b2b/api/v1/promotion/special/activity/pageActivityInfo',
    method: 'post',
    data: {
      current,
      size,
      ename,
      status,
      type,
      startTime,
      endTime
    }
  })
}

// 专场活动 - 保存按钮
export function specialActivitySubmit(addParams) {
  return request({
    url: '/b2b/api/v1/promotion/special/activity/submit',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 专场活动 - 停用专场活动
export function specialActivityStatus(id, status) {
  return request({
    url: '/b2b/api/v1/promotion/special/activity/status',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 专场活动 - 查询详情
export function specialActivityQueryById(id) {
  return request({
    url: '/b2b/api/v1/promotion/special/activity/queryById',
    method: 'get',
    params: {
      id
    }
  })
}

// 预约管理
export function appointmentPageList(
  current,
  size,
  specialActivityName,
  status,
  startTime,
  endTime,
  appointmentStartTime,
  appointmentEndTime,
  appointmentUserName,
  type,
  specialActivityEnterpriseName
) {
  return request({
    url: '/b2b/api/v1/promotion/special/appointment/pageList',
    method: 'post',
    data: {
      current,
      size,
      specialActivityName,
      status,
      startTime,
      endTime,
      appointmentStartTime,
      appointmentEndTime,
      appointmentUserName,
      type,
      specialActivityEnterpriseName
    }
  })
}

// -------------------------------- 策略满赠 --------------------------------
// 分页列表营销活动策略满赠-运营后台
export function strategyActivityPageList(
  current,
  size,
  // 活动名称
  strategyActivityName,
  // 活动状态
  status,
  // 活动进度
  progress,
  // 企业id
  eid,
  // 创建人名称
  createUserName,
  // 创建人手机号
  createTel,
  // 创建开始时间
  startTime,
  // 创建截止时间
  stopTime,
  // 活动类型
  strategyType,
  // 运营备注
  operatingRemark
) {
  return request({
    url: '/b2b/api/v1/strategy/activity/pageList',
    method: 'post',
    data: {
      current,
      size,
      strategyActivityName,
      status,
      progress,
      eid,
      createUserName,
      createTel,
      startTime,
      stopTime,
      strategyType,
      operatingRemark
    }
  })
}

// 分页列表营销活动参与明细-运营后台
export function strategyActivityRecordPageList(
  current,
  size,
  // 营销活动id
  marketingStrategyId,
  // 策略类型
  strategyType
) {
  return request({
    url: '/b2b/api/v1/strategy/activityRecord/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      strategyType
    }
  })
}

// 策略满赠-复制
export function strategyActivityCopy(id) {
  return request({
    url: '/b2b/api/v1/strategy/activity/copy',
    method: 'post',
    data: {
      id
    }
  })
}

// 策略满赠-停用
export function strategyActivityStop(id, status) {
  return request({
    url: '/b2b/api/v1/strategy/activity/stop',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 查询策略满赠详情-运营后台
export function strategyActivityQueryDetail(id) {
  return request({
    url: '/b2b/api/v1/strategy/activity/queryDetail',
    method: 'get',
    params: {
      id
    }
  })
}

// 优惠券活动列表
export function memberCouponActivityQueryListPage(
  current,
  size,
  // 优惠券名称
  name,
  // 优惠券ID
  id,
  createBeginTime,
  createEndTime,
  // 活动类型
  sponsorType,
  // 预算编号
  budgetCode,
  // 优惠券状态
  status,
  // 运营备注
  remark,
  // 创建人
  createUserName,
  activityStatus
) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      name,
      id,
      createBeginTime,
      createEndTime,
      sponsorType,
      budgetCode,
      status,
      remark,
      createUserName,
      activityStatus
    }
  })
}

// 策略满赠指定商家-添加商家-运营后台
export function strategyLimitSellerAdd(
  marketingStrategyId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/seller/add',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList
    }
  })
}

// 策略满赠指定商家-删除商家-运营后台
export function strategyLimitSellerDelete(
  marketingStrategyId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/seller/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList
    }
  })
}

// 策略满赠指定商家-已添加商家列表查询-运营后台
export function strategyLimitSellerPageList(
  current,
  size,
  // 营销活动id
  marketingStrategyId,
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
    url: '/b2b/api/v1/strategy/limit/seller/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      eid,
      ename,
      provinceCode,
      cityCode,
      regionCode
    }
  })
}

// 策略满赠平台SKU-平台SKU列表查询-运营后台
export function strategyLimitPlatformGoodsList(
  current,
  size,
  marketingStrategyId,
  // 商品ID
  standardId,
  // 规格ID
  sellSpecificationsId,
  // 商品名称
  goodsName,
  // 生产厂家
  manufacturer,
  // 以岭品 0-全部 1-是 2-否
  isYiLing
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/platformGoods/list',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      standardId,
      sellSpecificationsId,
      goodsName,
      manufacturer,
      isYiLing
    }
  })
}

// 策略满赠指定平台SKU-已添加平台SKU列表查询-运营后台
export function strategyLimitPlatformGoodsPageList(
  current,
  size,
  // 营销活动id
  marketingStrategyId,
  // 商品ID
  standardId,
  // 规格ID
  sellSpecificationsId,
  // 商品名称
  goodsName,
  // 生产厂家
  manufacturer,
  // 以岭品 0-全部 1-是 2-否
  isYiLing
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/platformGoods/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      standardId,
      sellSpecificationsId,
      goodsName,
      manufacturer,
      isYiLing
    }
  })
}

// 策略满赠指定平台SKU-添加平台SKU-运营后台
export function strategyLimitPlatformGoodsAdd(
  // 营销活动id
  marketingStrategyId,
  // 规格ID-添加当前页时使用
  sellSpecificationsIdList,
  // 商品ID
  standardIdPage,
  // 规格ID
  sellSpecificationsIdPage,
  // 商品名称
  goodsNamePage,
  // 生产厂家
  manufacturerPage,
  // 以岭品 0-全部 1-是 2-否
  isYiLing
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/platformGoods/add',
    method: 'post',
    data: {
      marketingStrategyId,
      sellSpecificationsIdList,
      standardIdPage,
      sellSpecificationsIdPage,
      goodsNamePage,
      manufacturerPage,
      isYiLing
    }
  })
}

// 策略满赠指定平台SKU-删除平台SKU-运营后台
export function strategyLimitPlatformGoodsDelete(
  // 营销活动id
  marketingStrategyId,
  // 规格ID-添加当前页时使用
  sellSpecificationsIdList,
  // 商品ID
  standardIdPage,
  // 规格ID
  sellSpecificationsIdPage,
  // 商品名称
  goodsNamePage,
  // 生产厂家
  manufacturerPage,
  // 以岭品 0-全部 1-是 2-否
  isYiLing
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/platformGoods/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      sellSpecificationsIdList,
      standardIdPage,
      sellSpecificationsIdPage,
      goodsNamePage,
      manufacturerPage,
      isYiLing
    }
  })
}

// 策略满赠指定店铺SKU-待添加店铺SKU列表查询-运营后台
export function strategyLimitEnterpriseGoodsList(current, size, marketingStrategyId, conditionSellerType, goodsName, goodsId, ename, yilingGoodsFlag, goodsStatus) {
  return request({
    url: '/b2b/api/v1/strategy/limit/enterpriseGoods/list',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      conditionSellerType,
      goodsName,
      goodsId,
      ename,
      yilingGoodsFlag,
      goodsStatus
    }
  })
}

// 策略满赠指定店铺SKU-已添加店铺SKU列表查询-运营后台
export function strategyLimitEnterpriseGoodsPageList(current, size, marketingStrategyId, conditionSellerType, goodsName, goodsId, ename, yilingGoodsFlag) {
  return request({
    url: '/b2b/api/v1/strategy/limit/enterpriseGoods/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      conditionSellerType,
      goodsName,
      goodsId,
      ename,
      yilingGoodsFlag
    }
  })
}

// 策略满赠指定店铺SKU-添加店铺SKU-运营后台
export function strategyLimitEnterpriseGoodsAdd(
  // 营销活动id
  marketingStrategyId,
  // 商品id集合
  goodsIdList,
  // 商品名称
  goodsNamePage,
  // 商品ID
  goodsIdPage,
  // 企业名称
  enamePage,
  // 以岭品
  yilingGoodsFlag,
  // 上下架状态
  goodsStatus
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/enterpriseGoods/add',
    method: 'post',
    data: {
      marketingStrategyId,
      goodsIdList,
      goodsNamePage,
      goodsIdPage,
      enamePage,
      yilingGoodsFlag,
      goodsStatus
    }
  })
}

// 策略满赠指定店铺SKU-删除店铺SKU-运营后台
export function strategyLimitEnterpriseGoodsDelete(
  // 营销活动id
  marketingStrategyId,
  // 商品id集合
  goodsIdList,
  // 商品名称
  goodsNamePage,
  // 商品ID
  goodsIdPage,
  // 企业名称
  enamePage,
  // 以岭品
  yilingGoodsFlag
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/enterpriseGoods/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      goodsIdList,
      goodsNamePage,
      goodsIdPage,
      enamePage,
      yilingGoodsFlag
    }
  })
}

// 策略满赠客户-选择需添加客户列表查询-运营后台
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

// 策略满赠客户-已添加客户列表查询-运营后台
export function strategyLimitBuyerPageList(
  current,
  size,
  marketingStrategyId,
  // 企业ID
  eid,
  // 企业名称
  ename
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/buyer/pageList',
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

// 策略满赠客户-添加客户-运营后台
export function strategyLimitBuyerAdd(
  // 营销活动id
  marketingStrategyId,
  // 企业id集合
  eidList,
  // 企业ID
  eidPage,
  // 企业名称
  enamePage
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/buyer/add',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList,
      eidPage,
      enamePage
    }
  })
}

// 策略满赠客户-删除客户-运营后台
export function strategyLimitBuyerDelete(
  // 营销活动id
  marketingStrategyId,
  // 企业id集合
  eidList,
  // 企业ID
  eidPage,
  // 企业名称
  enamePage
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/buyer/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList,
      eidPage,
      enamePage
    }
  })
}

// 策略满赠选择会员方案-选择需添加的会员方案-运营后台
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

// 策略满赠选择会员方案-已添加会员方案列表查询-运营后台
export function strategyLimitMemberPageList(
  current,
  size,
  marketingStrategyId
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/member/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId
    }
  })
}

// 策略满赠选择会员方案-添加会员方案-运营后台
export function strategyLimitMemberAdd(
  marketingStrategyId,
  memberId
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/member/add',
    method: 'post',
    data: {
      marketingStrategyId,
      memberId
    }
  })
}

// 策略满赠选择会员方案-删除会员方案-运营后台
export function strategyLimitMemberDelete(
  marketingStrategyId,
  memberId
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/member/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      memberId
    }
  })
}

// 策略满赠指定推广方会员商家-添加推广方会员商家-运营后台
export function strategyLimitPromoterMemberAdd(
  marketingStrategyId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/promoter/member/add',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList
    }
  })
}

// 策略满赠指定推广方会员商家-删除商家-运营后台
export function strategyLimitPromoterMemberDelete(
  marketingStrategyId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/strategy/limit/promoter/member/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList
    }
  })
}

// 策略满赠指定推广方会员-已添加推广方会员商家列表查询-运营后台
export function strategyLimitPromoterMemberPageList(
  current,
  size,
  // 营销活动id
  marketingStrategyId,
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
    url: '/b2b/api/v1/strategy/limit/promoter/member/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      eid,
      ename,
      provinceCode,
      cityCode,
      regionCode
    }
  })
}

// 策略满赠会员方案-待添加会员方案-运营后台
export function strategyStageMemberList(
  current,
  size,
  memberName
  ) {
  return request({
    url: '/b2b/api/v1/strategy/stageMember/list',
    method: 'post',
    data: {
      current,
      size,
      memberName
    }
  })
}

// 策略满赠会员方案-添加会员方案-运营后台
export function strategyStageMemberAdd(
  marketingStrategyId,
  memberIdList
  ) {
  return request({
    url: '/b2b/api/v1/strategy/stageMember/add',
    method: 'post',
    data: {
      marketingStrategyId,
      memberIdList
    }
  })
}

// 策略满赠会员方案-删除会员方案-运营后台
export function strategyStageMemberDelete(
  marketingStrategyId,
  memberIdList
  ) {
  return request({
    url: '/b2b/api/v1/strategy/stageMember/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      memberIdList
    }
  })
}

// 策略满赠会员方案-已添加会员方案-运营后台
export function strategyStageMemberPageList(
  current,
  size,
  // 营销活动id
  marketingStrategyId,
  memberName
  ) {
  return request({
    url: '/b2b/api/v1/strategy/stageMember/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      memberName
    }
  })
}

// 策略满赠主信息保存--上面的保存按钮
export function strategyActivitySave(addParams) {
  return request({
    url: '/b2b/api/v1/strategy/activity/save',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 策略满赠主信息保存--具体内容
export function strategyActivitySaveAll(addParams) {
  return request({
    url: '/b2b/api/v1/strategy/activity/saveAll',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// -------------------------------- 支付促销 --------------------------------
// 分页列表支付促销活动-运营后台
export function payPromptionActivityPageList(
  current,
  size,
  // 活动名称
  name,
  // 活动状态
  status,
  // 活动进度
  progress,
  // 企业id
  eid,
  // 创建人名称
  createUserName,
  // 创建人手机号
  createTel,
  // 创建开始时间
  startTime,
  // 创建截止时间
  stopTime,
  // 费用承担方 0-全部 1-平台承担；2-商家承担
  bear,
  // 活动类型
  sponsorType,
  // 运营备注
  operatingRemark
) {
  return request({
    url: '/b2b/api/v1/payPromption/activity/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      status,
      progress,
      eid,
      createUserName,
      createTel,
      startTime,
      stopTime,
      bear,
      sponsorType,
      operatingRemark
    }
  })
}

// 查询支付促销详情-运营后台
export function payPromptionActivityQueryDetail(id) {
  return request({
    url: '/b2b/api/v1/payPromption/activity/queryDetail',
    method: 'get',
    params: {
      id
    }
  })
}

// 支付促销主信息保存--上面的保存按钮
export function payPromptionActivitySave(addParams) {
  return request({
    url: '/b2b/api/v1/payPromption/activity/saveBasic',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 支付促销信息保存--具体内容
export function payPromptionActivitySaveRule(addParams) {
  return request({
    url: '/b2b/api/v1/payPromption/activity/saveRule',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 支付促销指定商家-添加商家-运营后台
export function payPromotionLimitSellerAdd(
  marketingStrategyId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/payPromotion/limit/seller/add',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList
    }
  })
}

// 支付促销指定商家-删除商家-运营后台
export function payPromotionyLimitSellerDelete(
  marketingStrategyId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/payPromotion/limit/seller/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList
    }
  })
}

// 支付促销指定商家-已添加商家列表查询-运营后台
export function payPromotionLimitSellerPageList(
  current,
  size,
  // 营销活动id
  marketingStrategyId,
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
    url: '/b2b/api/v1/payPromotion/limit/seller/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      eid,
      ename,
      provinceCode,
      cityCode,
      regionCode
    }
  })
}

// 支付促销指定推广方会员商家-添加推广方会员商家-运营后台
export function payPromptionLimitPromoterMemberAdd(
  marketingStrategyId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/promoter/member/add',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList
    }
  })
}

// 支付促销指定推广方会员商家-删除商家-运营后台
export function payPromptionLimitPromoterMemberDelete(
  marketingStrategyId,
  eidList
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/promoter/member/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList
    }
  })
}

// 支付促销指定推广方会员-已添加推广方会员商家列表查询-运营后台
export function payPromptionLimitPromoterMemberPageList(
  current,
  size,
  // 营销活动id
  marketingStrategyId,
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
    url: '/b2b/api/v1/payPromption/limit/promoter/member/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      eid,
      ename,
      provinceCode,
      cityCode,
      regionCode
    }
  })
}

// 支付促销指定平台SKU-已添加平台SKU列表查询-运营后台
export function payPromotionLimitPlatformGoodsPageList(
  current,
  size,
  // 营销活动id
  marketingStrategyId,
  // 商品ID
  standardId,
  // 规格ID
  sellSpecificationsId,
  // 商品名称
  goodsName,
  // 生产厂家
  manufacturer,
  // 以岭品 0-全部 1-是 2-否
  isYiLing
  ) {
  return request({
    url: '/b2b/api/v1/payPromotion/limit/platformGoods/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      standardId,
      sellSpecificationsId,
      goodsName,
      manufacturer,
      isYiLing
    }
  })
}

// 支付促销指定平台SKU-添加平台SKU-运营后台
export function payPromotionLimitPlatformGoodsAdd(
  // 营销活动id
  marketingStrategyId,
  // 规格ID-添加当前页时使用
  sellSpecificationsIdList,
  // 商品ID
  standardIdPage,
  // 规格ID
  sellSpecificationsIdPage,
  // 商品名称
  goodsNamePage,
  // 生产厂家
  manufacturerPage,
  // 以岭品 0-全部 1-是 2-否
  isYiLing
  ) {
  return request({
    url: '/b2b/api/v1/payPromotion/limit/platformGoods/add',
    method: 'post',
    data: {
      marketingStrategyId,
      sellSpecificationsIdList,
      standardIdPage,
      sellSpecificationsIdPage,
      goodsNamePage,
      manufacturerPage,
      isYiLing
    }
  })
}

// 支付促销指定平台SKU-删除平台SKU-运营后台
export function payPromotionLimitPlatformGoodsDelete(
  // 营销活动id
  marketingStrategyId,
  // 规格ID-添加当前页时使用
  sellSpecificationsIdList,
  // 商品ID
  standardIdPage,
  // 规格ID
  sellSpecificationsIdPage,
  // 商品名称
  goodsNamePage,
  // 生产厂家
  manufacturerPage,
  // 以岭品 0-全部 1-是 2-否
  isYiLing
  ) {
  return request({
    url: '/b2b/api/v1/payPromotion/limit/platformGoods/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      sellSpecificationsIdList,
      standardIdPage,
      sellSpecificationsIdPage,
      goodsNamePage,
      manufacturerPage,
      isYiLing
    }
  })
}

// 支付促销指定店铺SKU-已添加店铺SKU列表查询-运营后台
export function payPromptionLimitEnterpriseGoodsPageList(current, size, marketingStrategyId, conditionSellerType, goodsName, goodsId, ename, yilingGoodsFlag) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/enterpriseGoods/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId,
      conditionSellerType,
      goodsName,
      goodsId,
      ename,
      yilingGoodsFlag
    }
  })
}

// 支付促销指定店铺SKU-添加店铺SKU-运营后台
export function payPromptionLimitEnterpriseGoodsAdd(
  // 营销活动id
  marketingStrategyId,
  // 商品id集合
  goodsIdList,
  // 商品名称
  goodsNamePage,
  // 商品ID
  goodsIdPage,
  // 企业名称
  enamePage,
  // 以岭品
  yilingGoodsFlag,
  // 上下架状态
  goodsStatus
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/enterpriseGoods/add',
    method: 'post',
    data: {
      marketingStrategyId,
      goodsIdList,
      goodsNamePage,
      goodsIdPage,
      enamePage,
      yilingGoodsFlag,
      goodsStatus
    }
  })
}

// 支付促销指定店铺SKU-删除店铺SKU-运营后台
export function payPromptionLimitEnterpriseGoodsDelete(
  // 营销活动id
  marketingStrategyId,
  // 商品id集合
  goodsIdList,
  // 商品名称
  goodsNamePage,
  // 商品ID
  goodsIdPage,
  // 企业名称
  enamePage,
  // 以岭品
  yilingGoodsFlag
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/enterpriseGoods/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      goodsIdList,
      goodsNamePage,
      goodsIdPage,
      enamePage,
      yilingGoodsFlag
    }
  })
}

// 支付促销客户-已添加客户列表查询-运营后台
export function payPromptionLimitBuyerPageList(
  current,
  size,
  marketingStrategyId,
  // 企业ID
  eid,
  // 企业名称
  ename
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/buyer/pageList',
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

// 支付促销客户-添加客户-运营后台
export function payPromptionLimitBuyerAdd(
  // 营销活动id
  marketingStrategyId,
  // 企业id集合
  eidList,
  // 企业ID
  eidPage,
  // 企业名称
  enamePage
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/buyer/add',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList,
      eidPage,
      enamePage
    }
  })
}

// 支付促销客户-删除客户-运营后台
export function payPromptionLimitBuyerDelete(
  // 营销活动id
  marketingStrategyId,
  // 企业id集合
  eidList,
  // 企业ID
  eidPage,
  // 企业名称
  enamePage
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/buyer/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      eidList,
      eidPage,
      enamePage
    }
  })
}

// 支付促销选择会员方案-已添加会员方案列表查询-运营后台
export function payPromptionLimitMemberPageList(
  current,
  size,
  marketingStrategyId
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/member/pageList',
    method: 'post',
    data: {
      current,
      size,
      marketingStrategyId
    }
  })
}

// 支付促销选择会员方案-添加会员方案-运营后台
export function payPromptionLimitMemberAdd(
  marketingStrategyId,
  memberId
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/member/add',
    method: 'post',
    data: {
      marketingStrategyId,
      memberId
    }
  })
}

// 支付促销选择会员方案-删除会员方案-运营后台
export function payPromptionLimitMemberDelete(
  marketingStrategyId,
  memberId
  ) {
  return request({
    url: '/b2b/api/v1/payPromption/limit/member/delete',
    method: 'post',
    data: {
      marketingStrategyId,
      memberId
    }
  })
}

// 支付促销-参与次数分页列表
export function getPayPromotionParticipatePageById(
  current,
  size,
  // 活动id
  id
) {
  return request({
    url: '/b2b/api/v1/payPromption/activity/getPayPromotionParticipatePageById',
    method: 'post',
    data: {
      current,
      size,
      id
    }
  })
}

// 支付促销-复制
export function payPromptionActivityCopy(
  // 活动id
  id
) {
  return request({
    url: '/b2b/api/v1/payPromption/activity/copy',
    method: 'post',
    data: {
      id
    }
  })
}
  
// 支付促销-停用
export function payPromptionActivityStop(
  // 活动id
  id,
  // 状态：1-启用 2-停用 3-废弃
  status
) {
  return request({
    url: '/b2b/api/v1/payPromption/activity/stop',
    method: 'post',
    data: {
      id,
      status
    }
  })
}