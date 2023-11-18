import request from '@/subject/admin/utils/request'

// 用户名称搜索用户列表
export function queryCreatorByNmae(
  // 创建人名称
  name 
) {
  return request({
    url: '/b2b/api/v1/couponActivity/queryCreatorByNmae',
    method: 'post',
    data: {
      name
    }
  })
}

// 企业名称搜索企业列表
export function queryActivityEidByName(
  // 创建人名称
  name 
) {
  return request({
    url: '/b2b/api/v1/couponActivity/queryActivityEidByName',
    method: 'post',
    data: {
      name
    }
  })
}

// 优惠券活动列表
export function couponActivityQueryListPage(
  current,
  size,
  // 优惠券名称
  name,
  // 优惠券ID
  id,
  createBeginTime,
  createEndTime,
  sponsorType,
  budgetCode,
  status,
  createUser,
  availableEid,
  remark,
  activityStatus
) {
  return request({
    url: '/b2b/api/v1/couponActivity/queryListPage',
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
      createUser,
      availableEid,
      remark,
      activityStatus
    }
  })
}

// 添加优惠券活动基本信息
export function couponSaveBasic(addParams) {
  return request({
    url: '/b2b/api/v1/couponActivity/saveOrUpdateBasic',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 添加优惠券活动使用规则
export function couponSaveRules(addParams) {
  return request({
    url: '/b2b/api/v1/couponActivity/saveOrUpdateRules',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 复制
export function couponActivityCopy(id) {
  return request({
    url: '/b2b/api/v1/couponActivity/copy',
    method: 'get',
    params: {
      id
    }
  })
}

// 作废
export function couponActivityScrap(id) {
  return request({
    url: '/b2b/api/v1/couponActivity/scrap',
    method: 'get',
    params: {
      id
    }
  })
}

// 停用
export function couponActivityStop(id) {
  return request({
    url: '/b2b/api/v1/couponActivity/stop',
    method: 'get',
    params: {
      id
    }
  })
}

// 查看详情
export function getCouponActivity(id) {
  return request({
    url: '/b2b/api/v1/couponActivity/getCouponActivity',
    method: 'get',
    params: {
      id
    }
  })
}

// 增券
export function couponActivityIncrease(id, quantity, remark) {
  return request({
    url: '/b2b/api/v1/couponActivity/increase',
    method: 'post',
    data: {
      id,
      quantity,
      remark
    }
  })
}

// 查询供应商
export function queryEnterpriseListPage(
  current, 
  size, 
  eid, 
  ename, 
  provinceCode, 
  cityCode, 
  regionCode,
  //标签ID列表
  tagIds
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/queryEnterpriseListPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ename,
      // 所属省份编码
      provinceCode,
      // 所属城市编码
      cityCode,
      // 所属区域编码
      regionCode,
      tagIds
    }
  })
}

// 添加供应商
export function saveEnterpriseLimit(
  enterpriseLimitList
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/saveEnterpriseLimit',
    method: 'post',
    data: {
      enterpriseLimitList
    }
  })
}

// 删除供应商
export function deleteEnterpriseLimit(
  couponActivityId,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/deleteEnterpriseLimit',
    method: 'post',
    data: {
      couponActivityId,
      ids
    }
  })
}

// 查询已添加供应商
export function queryEnterpriseLimitListPage(
  current,
  size,
  couponActivityId,
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
    url: '/b2b/api/v1/couponActivity/queryEnterpriseLimitListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId,
      eid,
      ename,
      provinceCode,
      cityCode,
      regionCode
    }
  })
}

// 查询商品
export function queryGoodsListPage(current, size, couponActivityId, enterpriseLimit, goodsName, goodsId, ename, yilingGoodsFlag, goodsStatus) {
  return request({
    url: '/b2b/api/v1/couponActivity/queryGoodsListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId,
      enterpriseLimit,
      goodsName,
      goodsId,
      ename,
      yilingGoodsFlag,
      goodsStatus
    }
  })
}

// 添加商品
export function saveGoodsLimit(
  enterpriseLimit,
  goodsLimitList
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/saveGoodsLimit',
    method: 'post',
    data: {
      enterpriseLimit,
      goodsLimitList
    }
  })
}

// 删除商品
export function deleteGoodsLimit(
  couponActivityId,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/deleteGoodsLimit',
    method: 'post',
    data: {
      couponActivityId,
      ids
    }
  })
}

// 设置商品-添加商品-添加搜索结果
export function saveGoodsLimitSearchResult(
  // 优惠券活动ID
  couponActivityId,
  // 供应商限制（1-全部供应商；2-部分供应商）
  enterpriseLimit,
  // 商品名称
  goodsName,
  // 商品id
  goodsId,
  // 企业名称
  ename
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/saveGoodsLimitSearchResult',
    method: 'post',
    data: {
      couponActivityId,
      enterpriseLimit,
      goodsName,
      goodsId,
      ename
    }
  })
}

// 设置商品-删除商品搜索结果
export function deleteGoodsLimitSearchResult(
  // 优惠券活动ID
  couponActivityId,
  // 供应商限制（1-全部供应商；2-部分供应商）
  enterpriseLimit,
  // 商品名称
  goodsName,
  // 商品id
  goodsId,
  // 企业名称
  ename
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/deleteGoodsLimitSearchResult',
    method: 'post',
    data: {
      couponActivityId,
      enterpriseLimit,
      goodsName,
      goodsId,
      ename
    }
  })
}

// 查询已添加商品
export function queryGoodsLimitListPage(
  current,
  size,
  couponActivityId,
  goodsName,
  goodsId,
  ename,
  yilingGoodsFlag
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/queryGoodsLimitListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId,
      goodsName,
      goodsId,
      ename,
      yilingGoodsFlag
    }
  })
}

// 发放-查询供应商
export function giveQueryEnterpriseListPage(
  current,
  size,
  couponActivityId,
  ename,
  eid,
  etype,
  regionCode
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityGive/queryEnterpriseListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId,
      ename,
      eid,
      etype,
      regionCode
    }
  })
}

// 发放-查询供应商
export function giveQueryEnterpriseGiveRecordListPage(
  current,
  size,
  couponActivityId,
  ename,
  etype,
  regionCode
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityGive/queryEnterpriseGiveRecordListPage',
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

// 发放-添加供应商进行发券
export function addEnterpriseGiveRecord(
  unifyGiveNum,
  giveDetailList
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityGive/addEnterpriseGiveRecord',
    method: 'post',
    data: {
      unifyGiveNum,
      giveDetailList
    }
  })
}

// 发放-删除已发券供应商
export function sendDeleteEnterpriseGiveRecord(
  couponActivityId,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityGive/deleteEnterpriseGiveRecord',
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
    url: '/b2b/api/v1/couponActivity/queryGiveListPage',
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
    url: '/b2b/api/v1/couponActivity/queryUseListPage',
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
  couponActivityAutoId
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/queryAutoGiveListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityAutoId
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

// 自动发券 查询商品
export function autoGiveQueryGoodsListPage(current, size, goodsName, goodsId) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGive/queryGoodsListPage',
    method: 'post',
    data: {
      current,
      size,
      goodsName,
      goodsId
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
  couponActivityAutoId
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/queryAutoGiveListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityAutoId
    }
  })
}

// 查询优惠券剩余数量、可用供应商类型
export function getResidueCount(
  couponActivityId
  ) {
  return request({
    url: '/b2b/api/v1/couponActivity/getResidueCount',
    method: 'get',
    params: {
      couponActivityId
    }
  })
}

// 自助领券 设置会员-查询已添加会员企业
export function autoGetQueryMemberLimitListPage(current, size, couponActivityAutoGetId, eid, ename) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/queryMemberLimitListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityAutoGetId,
      eid,
      ename
    }
  })
}

// 自助领券  设置会员-添加会员企业
export function autoGetSaveEnterpriseLimit(
  enterpriseLimitList
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/saveEnterpriseLimit',
    method: 'post',
    data: {
      enterpriseLimitList
    }
  })
}

// 自助领券 设置会员-删除会员企业
export function autoGetDeleteEnterpriseLimit(
  couponActivityAutoGetId,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/deleteEnterpriseLimit',
    method: 'post',
    data: {
      couponActivityAutoGetId,
      ids
    }
  })
}

// -------------------------------- 会员优惠券 --------------------------------
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
  createUserName
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
      createUserName
    }
  })
}

// 列表-已发放优惠券查看
export function memberCouponQueryGiveListPage(
  current,
  size,
  // 优惠券活动id
  couponActivityId
  ) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/queryGiveListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId
    }
  })
}

// 列表-已使用会员优惠券查看
export function memberCouponQueryUseListPage(
  current,
  size,
  // 优惠券活动id
  couponActivityId
  ) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/queryUseListPage',
    method: 'post',
    data: {
      current,
      size,
      couponActivityId
    }
  })
}

// 列表-增券
export function memberCouponActivityIncrease(id, quantity, remark) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/increase',
    method: 'post',
    data: {
      id,
      quantity,
      remark
    }
  })
}

// 复制
export function memberCouponActivityCopy(id) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/copy',
    method: 'get',
    params: {
      id
    }
  })
}

// 作废
export function memberCouponActivityScrap(id) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/scrap',
    method: 'get',
    params: {
      id
    }
  })
}

// 停用
export function memberCouponActivityStop(id) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/stop',
    method: 'get',
    params: {
      id
    }
  })
}

// 会员优惠券活动-新增
export function memberCouponSaveOrUpdateBasic(addParams) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/saveOrUpdateBasic',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 查看详情
export function memberCouponGetCouponActivity(id) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/getCouponActivity',
    method: 'get',
    params: {
      id
    }
  })
}

// 查询会员规格列表-时长维度
export function queryMemberCouponSpecsList(current, size, id, name) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/queryMemberCouponSpecsList',
    method: 'post',
    data: {
      current,
      size,
      id,
      name
    }
  })
}

// 查询已经保存的会员规格-时长维度
export function queryMemberCouponRelationLimit(current, size, id, name) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/queryMemberCouponRelationLimit',
    method: 'post',
    data: {
      current,
      size,
      id,
      name
    }
  })
}

// 保存会员规格-时长维度
export function saveMemberCouponRelation(
  id,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/saveMemberCouponRelation',
    method: 'post',
    data: {
      id,
      ids
    }
  })
}

// 删除会员规格-时长维度
export function deleteMemberCouponRelation(
  ids
  ) {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/deleteMemberCouponRelation',
    method: 'post',
    data: {
      ids
    }
  })
}

// 查询会员规格列表-会员维度
export function queryMemberCouponRelationList() {
  return request({
    url: '/b2b/api/v1/memberCouponActivity/queryMemberCouponRelationList',
    method: 'post',
    data: {
    }
  })
}

// 查询已经保存的会员规格-会员维度
export function autoGetqueryMemberCouponRelationLimit(id) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/queryMemberCouponRelationLimit',
    method: 'post',
    data: {
      id
    }
  })
}

// 保存会员规格-会员维度
export function autoGetSaveMemberCouponRelation(
  id,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/saveMemberCouponRelation',
    method: 'post',
    data: {
      id,
      ids
    }
  })
}

// 删除会员规格-会员维度
export function autoGetDeleteMemberCouponRelation(
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/deleteMemberCouponRelation',
    method: 'post',
    data: {
      ids
    }
  })
}

// 设置商家-查询供应商
export function autoGetQueryEnterpriseListPage(current, size, eid, ename, provinceCode, cityCode, regionCode) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/queryEnterpriseListPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ename,
      // 所属省份编码
      provinceCode,
      // 所属城市编码
      cityCode,
      // 所属区域编码
      regionCode
    }
  })
}

// 设置商家-查询供应商-工业类型
export function autoGetQueryPromotionEnteroriseListPage(current, size, eid, ename, provinceCode, cityCode, regionCode) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/queryPromotionEnteroriseListPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ename,
      // 所属省份编码
      provinceCode,
      // 所属城市编码
      cityCode,
      // 所属区域编码
      regionCode
    }
  })
}

// 设置推广方-查询已添加推广方企业
export function queryPromotionListPage(current, size, couponActivityAutoGetId, eid, ename, provinceCode, cityCode, regionCode) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/queryPromotionListPage',
    method: 'post',
    data: {
      current,
      size,
      // 自主领取活动ID
      couponActivityAutoGetId,
      eid,
      ename,
      // 所属省份编码
      provinceCode,
      // 所属城市编码
      cityCode,
      // 所属区域编码
      regionCode
    }
  })
}

// 添加推广方企业
export function savePromotionEnterpriseLimit(
  enterpriseLimitList
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/savePromotionEnterpriseLimit',
    method: 'post',
    data: {
      enterpriseLimitList
    }
  })
}

// 设置商家-删除推广方企业
export function deletePromotionEnterpriseLimit(
  // 自动发券活动ID
  couponActivityAutoGetId,
  ids
  ) {
  return request({
    url: '/b2b/api/v1/couponActivityAutoGet/deletePromotionEnterpriseLimit',
    method: 'post',
    data: {
      couponActivityAutoGetId,
      ids
    }
  })
}