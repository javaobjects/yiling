import request from '@/subject/pop/utils/request'

// 优惠券活动列表 ---
export function couponActivityQueryListPage(
  current,
  size,
  // 优惠券名称
  name,
  // 优惠券ID
  id,
  createBeginTime,
  createEndTime,
  activityStatus,
  budgetCode,
  status,
  remark
) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      name,
      id,
      createBeginTime,
      createEndTime,
      activityStatus,
      budgetCode,
      status,
      remark
    }
  })
}

// 添加优惠券活动基本信息 ---
export function couponSaveBasic(addParams) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/saveOrUpdateBasic',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 添加优惠券活动使用规则 ---
export function couponSaveRules(addParams) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/saveOrUpdateRules',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 复制 ---
export function couponActivityCopy(id) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/copy',
    method: 'get',
    params: {
      id
    }
  })
}

// 作废 ---
export function couponActivityScrap(id) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/scrap',
    method: 'get',
    params: {
      id
    }
  })
}

// 停用 ---
export function couponActivityStop(id) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/stop',
    method: 'get',
    params: {
      id
    }
  })
}

// 查看详情 ---
export function getCouponActivity(id) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/getCouponActivity',
    method: 'get',
    params: {
      id
    }
  })
}

// 增券 ---
export function couponActivityIncrease(id, quantity, remark) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/increase',
    method: 'post',
    data: {
      id,
      quantity,
      remark
    }
  })
}

// 查询供应商 ---
export function queryEnterpriseListPage(current, size, ename) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/queryEnterpriseListPage',
    method: 'post',
    data: {
      current,
      size,
      ename
    }
  })
}

// 添加供应商 ---
export function saveEnterpriseLimit(
  enterpriseLimitList
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/saveEnterpriseLimit',
    method: 'post',
    data: {
      enterpriseLimitList
    }
  })
}

// 删除供应商 ---
export function deleteEnterpriseLimit(
  couponActivityId,
  ids
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/deleteEnterpriseLimit',
    method: 'post',
    data: {
      couponActivityId,
      ids
    }
  })
}

// 查询已添加供应商 ---
export function queryEnterpriseLimitListPage(
  current,
  size,
  couponActivityId,
  ename
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/queryEnterpriseLimitListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId,
      ename
    }
  })
}

// 查询商品 ---
export function queryGoodsListPage(current, size, goodsName, goodsId, yilingGoodsFlag, goodsStatus) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/queryGoodsListPage',
    method: 'post',
    data: {
      current,
      size,
      goodsName,
      goodsId,
      yilingGoodsFlag,
      goodsStatus
    }
  })
}

// 添加商品 ---
export function saveGoodsLimit(
  goodsLimitList
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/saveGoodsLimit',
    method: 'post',
    data: {
      goodsLimitList
    }
  })
}

// 删除商品 ---
export function deleteGoodsLimit(
  couponActivityId,
  ids
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/deleteGoodsLimit',
    method: 'post',
    data: {
      couponActivityId,
      ids
    }
  })
}

// 查询已添加商品 ---
export function queryGoodsLimitListPage(
  current,
  size,
  couponActivityId,
  goodsName,
  goodsId,
  yilingGoodsFlag
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/queryGoodsLimitListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId,
      goodsName,
      goodsId,
      yilingGoodsFlag
    }
  })
}

// 发放-查询供应商 ---
export function giveQueryEnterpriseListPage(
  current,
  size,
  couponActivityId,
  ename,
  etype,
  regionCode
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivityGive/queryEnterpriseListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId,
      ename,
      etype,
      regionCode
    }
  })
}

// 发放-查询供应商 ---
export function giveQueryEnterpriseGiveRecordListPage(
  current,
  size,
  couponActivityId,
  ename,
  etype,
  regionCode
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivityGive/queryEnterpriseGiveRecordListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId,
      ename,
      etype,
      regionCode
    }
  })
}

// 发放-添加供应商进行发券 ---
export function addEnterpriseGiveRecord(
  unifyGiveNum,
  giveDetailList
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivityGive/addEnterpriseGiveRecord',
    method: 'post',
    data: {
      unifyGiveNum,
      giveDetailList
    }
  })
}

// 发放-删除已发券供应商 ---
export function sendDeleteEnterpriseGiveRecord(
  couponActivityId,
  ids
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivityGive/deleteEnterpriseGiveRecord',
    method: 'post',
    data: {
      couponActivityId,
      ids
    }
  })
}

// 列表-已发放优惠券查看
export function queryGiveListPage(
  current,
  size,
  couponActivityId
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/queryGiveListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId
    }
  })
}

// 列表-已使用优惠券查看
export function queryUseListPage(
  current,
  size,
  couponActivityId
  ) {
  return request({
    url: '/admin/b2b/api/v1/couponActivity/queryUseListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId
    }
  })
}

// 自动发券-列表
export function couponActivityAutoGive1ueryListPage(
  current,
  size,
  // 自动发券活动名称
  name,
  // 自动发券活动id
  id,
  beginCreateTime,
  endCreateTime,
  type,
  activityStatus,
  status
) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      name,
      id,
      beginCreateTime,
      endCreateTime,
      type,
      activityStatus,
      status
    }
  })
}

// 列表-已发放优惠券查看
export function queryAutoGiveListPage(
  current,
  size,
  couponActivityAutoGiveId
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/queryAutoGiveListPage',
    method: 'get',
    params: {
      current,
      size,
      couponActivityAutoGiveId
    }
  })
}

// 列表-启用
export function couponActivityAutoGiveEnable(
  id
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/enable',
    method: 'get',
    params: {
      id
    }
  })
}

// 列表-停用
export function couponActivityAutoGiveStop(
  id
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/stop',
    method: 'get',
    params: {
      id
    }
  })
}

// 列表-作废
export function couponActivityAutoGivesScrap(
  id
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/scrap',
    method: 'get',
    params: {
      id
    }
  })
}

// 自动发放失败列表
export function queryGiveFailListPage(
  current,
  size,
  // 自动发券活动名称
  autoGiveName,
  // 自动发券活动id
  autoGiveId,
  beginCreateTime,
  endCreateTime,
  couponActivityId,
  eid,
  ename
) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/queryGiveFailListPage',
    method: 'post',
    data: {
      current,
      size,
      autoGiveName,
      autoGiveId,
      beginCreateTime,
      endCreateTime,
      couponActivityId,
      eid,
      ename
    }
  })
}

// 运营处理
export function couponActivityAutoGiveAutoGive(id) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/autoGive',
    method: 'post',
    data: {
      id
    }
  })
}

// 列表-保存/编辑基本信息
export function autoGiveSaveOrUpdateBasic(addParams) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/saveOrUpdateBasic',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 列表-保存/编辑规则信息
export function couponActivityAutoGiveSaveOrUpdateRules(addParams) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/saveOrUpdateRules',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 自动发券 设置商品-查询已添加商品
export function autoGiveQueryGoodsLimitListPage(current, size, couponActivityAutoGiveId, goodsName, goodsId) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/queryGoodsLimitListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityAutoGiveId,
      goodsName,
      goodsId
    }
  })
}

// 自动发券  添加商品
export function autoGiveSaveGoodsLimit(
  goodsLimitList
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/saveGoodsLimit',
    method: 'post',
    data: {
      goodsLimitList
    }
  })
}

// 自动发券 删除商品
export function autoGiveDeleteGoodsLimit(
  couponActivityAutoGiveId,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/deleteGoodsLimit',
    method: 'post',
    data: {
      couponActivityAutoGiveId,
      ids
    }
  })
}

// 自动发券 查看详情
export function autoGiveGetCouponActivity(id) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/getDetail',
    method: 'get',
    params: {
      id
    }
  })
}

// 自动发券 设置会员-查询会员企业
export function queryMemberListPage(current, size, eid, ename) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/queryMemberListPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ename
    }
  })
}

// 自动发券 设置会员-查询已添加会员企业
export function queryMemberLimitListPage(current, size, couponActivityAutoGiveId, eid, ename) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/queryMemberLimitListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityAutoGiveId,
      eid,
      ename
    }
  })
}

// 自动发券  设置会员-添加会员企业
export function autoGiveSaveEnterpriseLimit(
  enterpriseLimitList
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/saveEnterpriseLimit',
    method: 'post',
    data: {
      enterpriseLimitList
    }
  })
}

// 自动发券 设置会员-删除会员企业
export function autoGiveDeleteEnterpriseLimit(
  couponActivityAutoGiveId,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/deleteEnterpriseLimit',
    method: 'post',
    data: {
      couponActivityAutoGiveId,
      ids
    }
  })
}

// -------------------------------- 自助领券 --------------------------------
// 自助领券-列表
export function autoGetQueryListPage(
  current,
  size,
  // 自主领券活动名称
  name,
  // 自动发券活动id
  id,
  beginCreateTime,
  endCreateTime,
  activityStatus,
  status
) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      name,
      id,
      beginCreateTime,
      endCreateTime,
      activityStatus,
      status
    }
  })
}

// 列表-保存/编辑基本信息
export function autoGetSaveOrUpdateBasic(addParams) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/saveOrUpdateBasic',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 列表-保存/编辑规则信息
export function autoGetSaveOrUpdateRules(addParams) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/saveOrUpdateRules',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 自助领券 查看详情
export function autoGiveGetDetail(id) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/getDetail',
    method: 'get',
    params: {
      id
    }
  })
}

// 自助领券列表-启用
export function couponActivityAutoGetEnable(
  id
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/enable',
    method: 'get',
    params: {
      id
    }
  })
}

// 自助领券列表-停用
export function couponActivityAutoGetStop(
  id
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/stop',
    method: 'get',
    params: {
      id
    }
  })
}

// 自助领券列表-作废
export function couponActivityAutoGetScrap(
  id
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/scrap',
    method: 'get',
    params: {
      id
    }
  })
}

// 列表-已领取优惠券查看
export function autoGetQueryAutoGiveListPage(
  current,
  size,
  couponActivityAutoGetId
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/queryAutoGiveListPage',
    method: 'get',
    params: {
      current,
      size,
      couponActivityAutoGetId
    }
  })
}