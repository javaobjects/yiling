import { getDict } from '@/subject/admin/utils';
// 积分管理
//活动进度
export function integralRuleProgress() {
  return getDict('integral_rule_progress');
}
//积分规则适用平台
export function integralRuleUsePlatform() {
  return getDict('integral_rule_use_platform');
}
//支付方式
export function integralPaymentMethod() {
  return getDict('integral_payment_method');
}
//商品类型
export function integralGoodsType() {
  return getDict('integral_goods_type');
}