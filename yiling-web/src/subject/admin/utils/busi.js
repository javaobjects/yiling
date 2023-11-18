import { getDict } from '@/subject/admin/utils/index';

// 企业类型
export function enterpriseType() {
  return getDict('enterprise_type');
}

// 渠道类型
export function channelType() {
  return getDict('channel_type');
}

// 用户状态
export function userStatus() {
  return getDict('user_status');
}

// C端药+险业务类型
export function hmcType() {
  return getDict('hmc_type');
}

// 认证状态
export function enterpriseAuthStatus() {
  return getDict('enterprise_auth_status');
}

// 商品状态
export function goodsStatus() {
  return getDict('goods_status');
}

// 商品下架原因
export function goodsOutReason() {
  return getDict('goods_out_reason');
}

// 下载状态
export function downLoadStatus() {
  return getDict('export_status');
}

// 导入下载状态
export function excelExportStatus() {
  return getDict('excel_export_status');
}

/****************** 订单管理 *************************/

// 支付方式
export function paymentMethod() {
  return getDict('payment_method');
}

// 订单状态
export function orderStatus() {
  return getDict('order_status');
}

// 预订单审核状态
export function orderPreStatus() {
  return getDict('order_audit_status');
}

// 退货单类型
export function orderReturnStatus() {
  return getDict('order_return_type');
}

// 退货单状态
export function orderRejectStatus() {
  return getDict('order_return_status');
}

// 退货单审核状态
export function returnStatus() {
  return getDict('return_status');
}

// 订单支付状态
export function orderPayStatus() {
  return getDict('order_payment_status');
}

// 订单发票状态
export function orderTicketStatus() {
  return getDict('order_invoice_status');
}

// 订单管理订单来源
export function orderSource() {
  return getDict('order_source');
}

// 订单管理退货单订单来源
export function returnSource() {
  return getDict('return_source');
}

/****************** 质量监管 *************************/

// 标准库商品管制类型
export function standardGoodsControlType() {
  return getDict('standard_goods_control_type');
}
// 特殊成分
export function standardGoodsSpecialComposition() {
  return getDict('standard_goods_special_composition');
}
// 药品类型
export function standardGoodsType() {
  return getDict('standard_goods_type');
}
// 是否医保
export function standardGoodsIsYb() {
  return getDict('standard_goods_is_yb');
}
// 处方类型
export function standardGoodsOtcType() {
  return getDict('standard_goods_otc_type');
}
// 是否国产
export function standardGoodsIsCn() {
  return getDict('standard_goods_is_cn');
}
// 质量标准
export function standardGoodsQualityType() {
  return getDict('standard_goods_quality_type');
}
// 商品对应标准库 数据来源
export function goodsAuditSource() {
  return getDict('goods_audit_source');
}
// 商品经营所属范围
export function goodsBusinessScope() {
  return getDict('goods_business_scope');
}

/****************** 账期管理 *************************/

// 账期里的还款状态
export function repayStatus() {
  return getDict('payment_order_repayment_status');
}
/******************** 企业返利 ***********************/
// 审核状态
// 返利使用申请状态
export function agreementUseStatus() {
  return getDict('agreement_use_status');
}
// 返利申请单状态
export function agreementApplyStatus() {
  return getDict('agreement_apply_status');
}
// 协议返利形式
export function agreementRestitution() {
  return getDict('agreement_restitution');
}

/****************** 财务管理 *************************/
// b2b结算单类型
export function b2bSettlementType() {
  return getDict('b2b_settlement_type');
}
// b2b结算单状态
export function b2bSettlementStatus() {
  return getDict('b2b_settlement_status');
}
// 在线支付方式
export function paymentChannel() {
  return getDict('payment_channel');
}
// 重复支付
// 在线支付单据类型
export function paymentOrderType() {
  return getDict('payment_order_type');
}
// 重复支付处理状态
export function paymentOrderDealType() {
  return getDict('repeat_order_deal_type');
}
/****************** 企业审核 *************************/
// 数据来源
export function enterpriseSource() {
  // return getDict('enterprise_source')
  return getDict('enterprise_auth_source');
}
/****************** 系统管理 *************************/
//  日志管理-业务类型
export function logBusinessType() {
  return getDict('log_business_type');
}
//  日志管理-系统标识
export function logSystemType() {
  return getDict('log_system_type');
}
//  登录日志-应用ID枚举
export function loginLogApp() {
  return getDict('app_type');
}
// ERP管理
export function erpCustomerError() {
  return getDict('erp_customer_error');
}
// ERP对接级别
export function erpSyncLevel() {
  return getDict('erp_sync_level');
}
// 任务类型
export function taskFinishType() {
  return getDict('task_finish_type');
}
// 任务状态
export function taskStatus() {
  return getDict('task_status');
}
// erp 远程命令
export function erpClientCommand() {
  return getDict('erp_client_command');
}
// 佣金管理，任务类型
export function commissionTaskType() {
  return getDict('task_finish_type');
}

/****************** c端患者平台 *************************/
// 内容管理-引用业务线
export function displayLine() {
  return getDict('display_line');
}
// 投放位置
export function hmcAdvertisementPosition() {
  return getDict('hmc_advertisement_position');
}
// 注册来源
export function hmcRegisterSource() {
  return getDict('hmc_register_source');
}

// 佣金管理，用户类型：1-以岭人员 2-小三元 3-自然人
export function commissionUserType() {
  return getDict('user_type');
}
/****************** 统计报表 *************************/
//  统计报表-数据类型
export function orderRecordType() {
  return getDict('order_record_report_export');
}
//  统计报表-商品品类
export function orderCategoryType() {
  return getDict('order_category_report_export');
}

// 退款来源
export function financeOrderFundType() {
  return getDict('finance_order_fund_type');
}

/****************** 流向封存 *************************/
//  流向类型
export function erpFlowType() {
  return getDict('erp_flow_type');
}
//  封存状态
export function erpFlowSealedStatus() {
  return getDict('erp_flow_sealed_status');
}

// 数据报表使用，订单来源
export function erpSaleFlowSource() {
  return getDict('erp_sale_flow_source');
}
// 数据报表使用，返利状态
export function dataTableReportStatus() {
  return getDict('report_status');
}

/********勿再增加，新增字典到busi文件夹*******/
