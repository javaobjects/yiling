import { getDict } from '@/subject/admin/utils';

// 活动进度：1-未开始 2-进行中 3-已结束
export function lotteryActivityProgress() {
  return getDict('lottery_activity_progress');
}

// 活动平台
export function lotteryActivityPlatform() {
  return getDict('lottery_activity_platform');
}

// 指定范围客户企业类型
export function lotteryActivityGiveEnterpriseType() {
  return getDict('lottery_activity_give_enterprise_type');
}

// 奖品类型
export function lotteryActivityRewardType() {
  return getDict('lottery_activity_reward_type');
}

// 获取方式
export function lotteryActivityGetType() {
  return getDict('lottery_activity_get_type');
}

// 重复执行
export function lotteryActivityLoopGive() {
  return getDict('lottery_activity_loop_give');
}