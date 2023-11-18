import { getDict } from './index';

/****************** 企业管理 *************************/

// 企业类型
export function enterpriseType() {
  return getDict('enterprise_type');
}

// 渠道类型
export function channelType() {
  return getDict('channel_type');
}

// 认证状态
export function enterpriseAuthStatus() {
  return getDict('enterprise_auth_status');
}

/****************** 商品管理 *************************/

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

// 专利非专利
export function goodsPatent() {
  return getDict('goods_patent');
}

// 商品审核状态
export function goodsAuditStatus() {
  return getDict('goods_audit_status');
}

// 商品可编辑类目
export function goodsEditColumn() {
  return getDict('goods_edit_column');
}
// 商品库存类型
export function subscription() {
  return getDict('inventory_subscription_type');
}

/****************** 权限管理 *************************/

/****************** 客户管理 *************************/

// 支付方式
export function paymentMethod() {
  return getDict('payment_method');
}

/****************** 采购管理 *************************/

// 订单管理退货单订单来源
export function returnSource() {
  return getDict('return_source');
}

// 订单状态
export function orderStatus() {
  return getDict('order_status');
}

// 预订单审核状态
export function orderPreStatus() {
  return getDict('order_audit_status');
}

// 退货状态
export function orderReturnStatus() {
  return getDict('order_return_type');
}

// 退货单审核状态
export function returnStatus() {
  return getDict('order_return_status');
}

// 退货单发票节点
export function orderInvoiceStatus() {
  return getDict('order_return_invoice_status');
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

// 快递公司
export function orderDeliveryCompany() {
  return getDict('order_delivery_company');
}

// 转换规则
export function orderInvoiceTransitionRule() {
  return getDict('order_invoice_transition_rule');
}

/****************** 账期管理 *************************/

// 账期状态
export function periodStatus() {
  return getDict('payment_days_status');
}

// 账期里的还款状态
export function repayStatus() {
  return getDict('payment_days_repayment_status');
}

// 账期里的审核状态
export function periodAuditStatus() {
  return getDict('payment_days_temporary_audit_status');
}
/****************** 账期管理 *************************/

/****************** 企业返利 *************************/
// 返利使用申请状态
export function agreementUseStatus() {
  return getDict('agreement_use_status');
}
// 协议返利形式
export function agreementRestitution() {
  return getDict('agreement_restitution');
}
// 返利申请单状态
export function agreementApplyStatus() {
  return getDict('agreement_apply_status');
}

/****************** 企业返利 *************************/

/****************** 添加商品 *************************/

// 药品类型
export function standardGoodsType() {
  return getDict('standard_goods_type');
}
// 是否国产
export function standardGoodsIsCn() {
  return getDict('standard_goods_is_cn');
}

/****************** 添加商品 *************************/
// 企业资质类型
export function enterpriseCertificateType() {
  return getDict('enterprise_certificate_type');
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
// 支付类型
export function paymentChannel() {
  return getDict('payment_channel');
}
// 企业收款账户 审核状态
export function b2bReceiptAccountStatus() {
  return getDict('b2b_receipt_account_status');
}
/****************** zt商品管理产品线 *************************/
// 产品线
export function goodsLine() {
  return getDict('goods_line');
}

/****************** b2b 企业采购申请状态 *************************/
// 申请状态
export function enterprisePurchaseApplyStatus() {
  return getDict('enterprise_purchase_apply_status');
}

/******************协议V2.0协议条款 *************************/
// 甲方类型
export function agreementFirstType() {
  return getDict('agreement_first_type');
}
// 协议类型
export function agreementType() {
  return getDict('agreement_type');
}

// 保证金支付方
export function agreementMarginPayer() {
  return getDict('agreement_margin_payer');
}

// 协议附件类型
export function agreementAttachmentType() {
  return getDict('agreement_attachment_type');
}

// 购进渠道
export function agreementSupplySalesBuyChannel() {
  return getDict('agreement_supply_sales_buy_channel');
}

// 控销类型
export function agreementControlSaleType() {
  return getDict('agreement_control_sale_type');
}

// 控销类型
export function agreementPayMethod() {
  return getDict('agreement_pay_method');
}

// 送货方式
export function agreementDeliveryType() {
  return getDict('agreement_delivery_type');
}

// 商品返利规则设置方式
export function agreementGoodsRebateRuleType() {
  return getDict('agreement_goods_rebate_rule_type');
}

// 返利支付方
export function agreementRebatePay() {
  return getDict('agreement_rebate_pay');
}

// 返利兑付方式
export function agreementRebateCashType() {
  return getDict('agreement_rebate_cash_type');
}

// 返利兑付时间
export function agreementRebateCashTime() {
  return getDict('agreement_rebate_cash_time');
}

// 非商品返利方式
export function agreementNotProductRebateType() {
  return getDict('agreement_not_product_rebate_type');
}

// 金额类型
export function agreementNotProductRebateAmountType() {
  return getDict('agreement_not_product_rebate_amount_type');
}

// 返利标准
export function agreementRebateTaskStandard() {
  return getDict('agreement_rebate_task_standard');
}

// 返利阶梯条件计算方法
export function agreementRebateStageMethod() {
  return getDict('agreement_rebate_stage_method');
}

// 返利计算规则
export function agreementRebateCalculateRule() {
  return getDict('agreement_rebate_calculate_rule');
}

// 返利计算规则类型
export function agreementRebateRuleType() {
  return getDict('agreement_rebate_rule_type');
}

// 时段类型设置
export function agreementTimeSegmentTypeSet() {
  return getDict('agreement_time_segment_type_set');
}

// 核心商品组关联性
export function agreementCoreCommodityGroupRelevance() {
  return getDict('agreement_core_commodity_group_relevance');
}

// 核心商品组任务量未完成时
export function agreementCoreCommodityGroupFail() {
  return getDict('agreement_core_commodity_group_fail');
}

// 单据类型
export function agreementBillsType() {
  return getDict('agreement_bills_type');
}

// 协议审核状态
export function agreementAuthStatus() {
  return getDict('agreement_auth_status');
}

// 协议生效状态
export function agreementEffectStatus() {
  return getDict('agreement_effect_status');
}

// KA协议类型
export function kaAgreementType() {
  return getDict('ka_agreement_type');
}

//-----------------协议审核相关字典------------------
// 协议审核驳回原因类型
export function agreementAuditRejectType() {
  return getDict('agreement_audit_reject_type');
}

// 基本信息
export function agreementRejectBasic() {
  return getDict('agreement_reject_basic');
}

// 协议主条款
export function agreementRejectMainTerms() {
  return getDict('agreement_reject_main_terms');
}

// 商业供货协议条款
export function agreementRejectSettlementTerms() {
  return getDict('agreement_reject_settlement_terms');
}

// 协议供销条款
export function agreementRejectSupplySalesTerms() {
  return getDict('agreement_reject_supply_sales_terms');
}

// 协议返利条款
export function agreementRejectRebateTerms() {
  return getDict('agreement_reject_rebate_terms');
}

// 协议附件
export function agreementRejectAttachment() {
  return getDict('agreement_reject_attachment');
}

/****************** pop erp流向  *************************/
// 企业库存，订单来源
export function erpGoodsBatchFlowSource() {
  return getDict('erp_goods_batch_flow_source');
}
// 企业销售流向  订单来源
export function erpSaleFlowSource() {
  return getDict('erp_sale_flow_source');
}
// 企业采购流向 订单来源
export function erpPurchaseFlowSource() {
  return getDict('erp_purchase_flow_source');
}

/***cmp 药险 商家后台 字典表***/
//  药险订单状态
export function hmcOrderStatus() {
  return getDict('hmc_order_status');
}
// cmp配送状态
export function hmcDeliveryStatus() {
  return getDict('hmc_deliver_type');
}
// 保单状态
export function hmcPolicyStatus() {
  return getDict('hmc_policy_status');
}
// 药品终端结算状态
export function hmcSettlementStatus() {
  return getDict('hmc_terminal_settle_status');
}

// 药品终端订单类型
export function hmcOrderType() {
  return getDict('hmc_order_type');
}
//  药品终端支付状态
export function hmcPaymentStatus() {
  return getDict('hmc_payment_status');
}

/********勿再增加，新增字典到busi文件夹*******/
