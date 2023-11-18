import { getDict } from '@/subject/admin/utils';

// 数据报表-流向返利报表-详情-动销渠道
export function syncPurChannel() {
  return getDict('sync_pur_channel');
}
// 数据报表-订单返利报表/流向返利报表-返利状态
export function orderRewardStatus() {
  return getDict('order_reward_status');
}
// 返利总表-流向返利详情页-购进渠道
export function reportPurchaseChannel() {
  return getDict('report_purchase_channel');
}
// 返利总表-返利类型
export function reportType() {
  return getDict('report_type');
}
// 返利总表-会员来源
export function reportMemberSource() {
  return getDict('report_member_source');
}
// 返利总表-返利状态
export function reportRebateStatus() {
  return getDict('report_rebate_status');
}
// 返利总表-标识状态
export function reportOrderIdent() {
  return getDict('report_order_ident');
}
// 订单返利报表-标志状态为异常订单的类型
export function reportAbnormalReason() {
  return getDict('report_abnormal_reason');
}
// 报表参数-促销活动设置/阶梯规则设置-添加/修改商品-订单来源
export function reportOrderSource() {
  return getDict('report_order_source');
}
// 报表参数-商家品与以岭品对应关系-商品关系标签
export function flowGoodsRelationLabel() {
  return getDict('flow_goods_relation_label');
}