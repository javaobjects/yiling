import { getDict } from '@/subject/admin/utils';

// 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
export function strategyType() {
  return getDict('strategy_type');
}
//b2b管理 - banner - 页面配置
export function b2bAppBannerType() {
  return getDict('b2b_app_banner_type');
}
//b2b管理 - 金刚位 - 页面配置
export function b2bAppVajraPositionType() {
  return getDict('b2b_app_vajra_position_type');
}