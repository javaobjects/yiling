// 积分消耗接口
import request from '@/subject/admin/utils/request'
export function queryListPage(
  //规则名称
  name,
  //执行状态：1-启用 2-停用
  status,
  //执行进度：1-未开始 2-进行中 3-已结束
  progress,
  //创建人名称
  createUserName,
  //创建人手机号
  mobile,
  //规则生效时间
  startTime,
  //规则失效时间
  endTime,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/integralUseRule/queryListPage',
    method: 'post',
    data: {
      name,
      status,
      progress,
      createUserName,
      mobile,
      startTime,
      endTime,
      current,
      size
    }
  })
}
//停用规则
export function updateStatus(
  //规则id
  id
){
  return request({
    url: '/b2b/api/v1/integralUseRule/updateStatus',
    method: 'get',
    params: {
      id
    }
  })
}
//保存积分消耗规则基本信息
export function saveBasic(
  //规则名称
  name,
  //规则生效时间
  startTime,
  //规则失效时间
  endTime,
  //规则说明
  description,
  //行为ID
  behaviorId,
  id
){
  return request({
    url: '/b2b/api/v1/integralUseRule/saveBasic',
    method: 'post',
    data: {
      name,
      startTime,
      endTime,
      description,
      behaviorId,
      id
    }
  })
}
// 查看详情
export function getIntegralUseRule(
  //规则id
  id
) {
  return request({
    url: '/b2b/api/v1/integralUseRule/get',
    method: 'get',
    params: {
      id
    }
  })
}
//查询抽奖活动分页列表
export function lotteryActivityPage(
  //平台类型：1-B端 2-C端
  platformType,
  //活动名称
  activityName,
  //活动进度：1-未开始 2-进行中 3-已结束（见字典：lottery_activity_progress）
  progress,
  //不等于该活动进度：1-未开始 2-进行中 3-已结束
  neProgress,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/lotteryActivity/queryListPage',
    method: 'post',
    data: {
      platformType,
      //活动名称
      activityName,
      progress,
      neProgress,
      current,
      size
    }
  })
}
// 保存参与抽奖配置
export function saveLotteryConfig(
  //消耗规则ID
  useRuleId,
  //抽奖活动ID
  lotteryActivityId,
  //消耗积分值
  useIntegralValue,
  //消耗总次数限制
  useSumTimes,
  //单用户每天消耗总次数限制
  everyDayTimes
){
  return request({
    url: '/b2b/api/v1/integralUseRule/saveLotteryConfig',
    method: 'post',
    data: {
      useRuleId,
      lotteryActivityId,
      useIntegralValue,
      useSumTimes,
      everyDayTimes
    }
  })
}
//复制
export function integralUseRuleCopy(
  //消耗规则ID
  id
) {
  return request({
    url: '/b2b/api/v1/integralUseRule/copy',
    method: 'get',
    params: {
      id
    }
  })
}