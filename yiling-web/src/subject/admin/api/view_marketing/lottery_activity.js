import request from '@/subject/admin/utils/request'

// 查询抽奖活动分页列表
export function lotteryActivityQueryListPage(
  current,
  size,
  // 抽奖活动ID
  lotteryActivityId,
  // 平台类型：1-B端 2-C端
  platformType,
  // 活动名称
  activityName,
  // 活动状态：1-启用 2-停用
  status,
  // 活动进度：1-未开始 2-进行中 3-已结束
  progress,
  // 企业名称
  ename,
  // 创建人名称
  createUserName,
  // 创建人手机号
  mobile,
  startCreateTime,
  endCreateTime,
  // 活动分类：1-平台活动 2-商家活动
  category,
  // 运营备注
  opRemark,
  // 不等于该活动进度：1-未开始 2-进行中 3-已结束
  neProgress
) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      lotteryActivityId,
      platformType,
      activityName,
      status,
      progress,
      ename,
      createUserName,
      mobile,
      startCreateTime,
      endCreateTime,
      category,
      opRemark,
      neProgress
    }
  })
}

// 复制抽奖活动
export function lotteryActivityCopyLottery(id) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/copyLottery',
    method: 'get',
    params: {
      id
    }
  })
}

// 停用抽奖活动
export function lotteryActivityStop(id) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/stop',
    method: 'get',
    params: {
      id
    }
  })
}

// 关联活动分页列表
export function strategyActivityLotteryPageList(
  current,
  size,
  // 抽奖活动ID
  lotteryActivityId
) {
  return request({
    url: '/b2b/api/v1/strategy/activity/lotteryPageList',
    method: 'post',
    data: {
      current,
      size,
      lotteryActivityId
    }
  })
}

// 抽奖机会明细分页列表
export function lotteryActivityQueryLotteryGetPage(
  current,
  size,
  // 抽奖活动ID
  lotteryActivityId
) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/queryLotteryGetPage',
    method: 'post',
    data: {
      current,
      size,
      lotteryActivityId
    }
  })
}

// 抽奖次数/中奖次数分页列表
export function lotteryActivityQueryJoinDetailPage(
  current,
  size,
  // 页面类型：1-抽奖次数弹窗 2-中奖次数弹窗
  pageType,
  // 抽奖活动ID
  lotteryActivityId
) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/queryJoinDetailPage',
    method: 'post',
    data: {
      current,
      size,
      pageType,
      lotteryActivityId
    }
  })
}

// 兑付奖品
export function lotteryActivityCashReward(addParams) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/cashReward',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 修改兑付信息
export function lotteryActivityUpdateCashInfo(addParams) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/updateCashInfo',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 选择客户分页列表
export function lotteryCustomerScopeQueryCustomerPage(
  current,
  size,
  // 企业ID
  id,
  // 企业名称
  name
) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/queryCustomerPage',
    method: 'post',
    data: {
      current,
      size,
      id,
      name
    }
  })
}

// 已添加客户分页列表
export function lotteryCustomerScopeQueryHadAddCustomerPage(
  current,
  size,
  // 抽奖活动ID
  lotteryActivityId,
  // 企业ID
  id,
  // 企业名称
  name
) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/queryHadAddCustomerPage',
    method: 'post',
    data: {
      current,
      size,
      lotteryActivityId,
      id,
      name
    }
  })
}

// 添加指定客户
export function lotteryCustomerScopeAddCustomer(
  // 抽奖活动ID
  lotteryActivityId,
  // 1-单个添加 2-添加当前页 3-添加搜索结果
  type,
  // 企业ID集合
  idList,
  // 企业ID
  id,
  // 企业名称
  name
  ) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/addCustomer',
    method: 'post',
    data: {
      lotteryActivityId,
      type,
      idList,
      id,
      name
    }
  })
}

// 删除指定客户
export function lotteryCustomerScopeDeleteCustomer(
  // 抽奖活动ID
  lotteryActivityId,
  // 1-单个添加 2-添加当前页 3-添加搜索结果
  type,
  // 企业ID集合
  idList,
  // 企业ID
  id,
  // 企业名称
  name
  ) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/deleteCustomer',
    method: 'post',
    data: {
      lotteryActivityId,
      type,
      idList,
      id,
      name
    }
  })
}

// 选择方案会员分页列表
export function lotteryCustomerScopeQueryMemberPage(
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/queryMemberPage',
    method: 'post',
    data: {
      current,
      size
    }
  })
}

// 已添加会员分页列表
export function lotteryCustomerScopeQueryHadAddMemberPage(
  current,
  size,
  lotteryActivityId
) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/queryHadAddMemberPage',
    method: 'post',
    data: {
      current,
      size,
      lotteryActivityId
    }
  })
}

// 添加会员
export function lotteryCustomerScopeAddMember(
  lotteryActivityId,
  memberId
) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/addMember',
    method: 'post',
    data: {
      lotteryActivityId,
      memberId
    }
  })
}

// 删除会员
export function lotteryCustomerScopeDeleteMember(
  lotteryActivityId,
  memberId
) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/deleteMember',
    method: 'post',
    data: {
      lotteryActivityId,
      memberId
    }
  })
}

// 选择推广方分页列表
export function lotteryCustomerScopeQueryPromoterPage(
  current,
  size,
  // 企业ID
  id,
  // 企业名称
  name
) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/queryPromoterPage',
    method: 'post',
    data: {
      current,
      size,
      id,
      name
    }
  })
}

// 已添加推广方分页列表
export function lotteryCustomerScopeQueryHadAddPromoterPage(
  current,
  size,
  // 抽奖活动ID
  lotteryActivityId,
  // 企业ID
  id,
  // 企业名称
  name
) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/queryHadAddPromoterPage',
    method: 'post',
    data: {
      current,
      size,
      lotteryActivityId,
      id,
      name
    }
  })
}

// 添加推广方
export function lotteryCustomerScopeAddPromoter(
  // 抽奖活动ID
  lotteryActivityId,
  // 1-单个添加 2-添加当前页 3-添加搜索结果
  type,
  // 企业ID集合
  idList,
  // 企业ID
  id,
  // 企业名称
  name
  ) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/addPromoter',
    method: 'post',
    data: {
      lotteryActivityId,
      type,
      idList,
      id,
      name
    }
  })
}

// 删除推广方
export function lotteryCustomerScopeDeletePromoter(
  // 抽奖活动ID
  lotteryActivityId,
  // 1-单个添加 2-添加当前页 3-添加搜索结果
  type,
  // 企业ID集合
  idList,
  // 企业ID
  id,
  // 企业名称
  name
  ) {
  return request({
    url: '/b2b/api/v1/lotteryCustomerScope/deletePromoter',
    method: 'post',
    data: {
      lotteryActivityId,
      type,
      idList,
      id,
      name
    }
  })
}

// 查询抽奖活动详情
export function lotteryActivityGet(id) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/get',
    method: 'get',
    params: {
      id
    }
  })
}

// 添加或修改抽奖活动基本信息
export function lotteryActivitySaveActivityBasic(addParams) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/saveActivityBasic',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 修改抽奖活动设置信息
export function lotteryActivitySaveActivitySetting(addParams) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/saveActivitySetting',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 修改奖品设置信息
export function lotteryActivityUpdateRewardSetting(activityRewardSettingList) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/updateRewardSetting',
    method: 'post',
    data: {
      activityRewardSettingList
    }
  })
}

// 参与规则分页列表
export function lotteryActivityQueryRulePage(
  current,
  size,
  // 平台类型：1-B端 2-C端
  usePlatform
) {
  return request({
    url: '/b2b/api/v1/lotteryActivity/queryRulePage',
    method: 'post',
    data: {
      current,
      size,
      usePlatform
    }
  })
}