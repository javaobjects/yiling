import { getDict } from '@/subject/admin/utils';

// 企业库存，订单来源
export function erpGoodsBatchFlowSource() {
  return getDict('erp_goods_batch_flow_source');
}
// 企业采购流向 订单来源
export function erpPurchaseFlowSource() {
  return getDict('erp_purchase_flow_source');
}
// 企业销售流向  订单来源
export function erpSaleFlowSource() {
  return getDict('erp_sale_flow_source');
}
// erp管理-对接企业管理-工具对接方式
export function erpClientFlowMode() {
  return getDict('erp_client_flow_mode');
}
// erp同步操作类型
export function erpOperType() {
  return getDict('erp_oper_type');
}
// 有无销售
export function erpPurchaseSaleFlag() {
  return getDict('erp_purchase_sale_flag');
}
//终端激活状态
export function erpClientStatus() {
  return getDict('erp_client_status');
}