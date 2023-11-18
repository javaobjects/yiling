package com.yiling.framework.common.util;

import java.util.List;

import cn.hutool.core.collection.ListUtil;

/**
 * 系统常量
 *
 * @author: xuan.zhou
 * @date: 2021/5/10
 */
public class Constants {

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String PARAM_APP_KEY = "appKey";
    public static final String PARAM_TIMESTAMP = "timestamp";
    public static final String PARAM_QUERY_PARAMS = "queryParams";
    public static final String PARAM_BODY = "body";
    public static final String PARAM_SIGN = "sign";

    public static final String SIGN_METHOD_MD5 = "md5";
    public static final String SIGN_METHOD_HMAC = "hmac";

    /**
     * 开发、测试环境名称
     */
    public static final List<String> DEBUG_ENV_LIST   = ListUtil.toList("dev-connect","dev","test-connect","test2-connect","test", "test2");

    /**
     * 流向任务编号定义
     */
    public static final List<String> FLOW_TASK_NO   = ListUtil.toList("erp_purchase_flow","erp_sale_flow","erp_goods_batch_flow");

    /**
     * 链路跟踪字段名
     */
    public static final String TRACE_ID = "traceId";

    /**
     * 以岭企业ID
     */
    public static final Long YILING_EID = 1L;

    public static final String ZERO = "0";
    /**
     * esb_organization 商务部org_id
     */
    public static final Long COMMERCE_ORG_ID = 10410L;
    /**
     * esb_organization 区域运营管理部org_id
     */
    public static final Long MARKET_ORG_ID = 12325L;
    /**
     * 大运河工业企业ID
     */
    public static final Long DAYUNHE_EID = 4L;

    /**
     * 大运河商业企业ID
     */
    public static final Long DAYUNHE_BUSINESS_EID = 5L;

    /** ======================================== 网关向下级服务传递的Header值 - begin ================================================ */
    public static final String CURRENT_APP_ID = "appId";
    public static final String CURRENT_USER_ID = "userId";
    public static final String CURRENT_USER_CODE = "userCode";
    public static final String CURRENT_USER_TYPE = "userType";
    public static final String CURRENT_USER_IP = "userIP";
    public static final String CURRENT_EID     = "eid";
    public static final String CURRENT_ETYPE      = "etype";
    public static final String CURRENT_CHANNEL_ID = "channelId";
    public static final String CURRENT_EMPLOYEE_ID = "employeeId";
    public static final String CURRENT_IS_ADMIN = "isAdmin";
    /** ======================================== 网关向下级服务传递的Header值 - end ================================================ */

    /**
     * 手机号正则表达式
     */
    public static final String REGEXP_MOBILE = "^1[3-9]\\d{9}$";

    /**
     * 虚拟手机号正则表达式，1开头的11位即可
     */
    public static final String REGEXP_SPECIAL_MOBILE = "^1\\d{10}$";

    /**
     * 用户名正则表达式：字母、数字、下划线，4-16位
     */
    public static final String REGEXP_USERNAME = "^[a-zA-Z0-9_-]{4,16}$";

    /**
     * 密码正则表达式：字母、数字、下划线，8-16位
     */
    public static final String REGEXP_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";

    /**
     * 验证框架自定义错误信息内容前缀
     */
    public static final String VALIDATION_CUSTOM_MESSAGE_PREFIX = "^";

    /**
     * 下划线分隔符
     */
    public static final String SEPARATOR_UNDERLINE = "_";

    /**
     * 中划线分隔符
     */
    public static final String SEPARATOR_MIDDLELINE = "-";



    /** ======================================== MQ Topic & Tag - begin ================================================ */
    /** 新短信创建 */
    public static final String TOPIC_SMS_CREATED   = "topic_sms_created";

    /** 新订单创建 */
    public static final String TOPIC_ORDER_CREATED = "topic_order_created";
    /** 订单已支付 */
    public static final String TOPIC_ORDER_PAID = "topic_order_paid";
    /** 订单已审核 */
    public static final String TOPIC_ORDER_AUDITED = "topic_order_audited";
    /** 订单已取消 */
    public static final String TOPIC_ORDER_CANCELED = "topic_order_canceled";
    /** 订单已发货 */
    public static final String TOPIC_ORDER_DELIVERED = "topic_order_delivered";
    /** 订单已签收 */
    public static final String TOPIC_ORDER_RECEIVED = "topic_order_received";

    /** 退货单生成MQ(包含订单发货，订单收货，订单反审核) */
    public static final String TOPIC_ORDER_RETURN = "topic_order_return";

    public static final String TAG_ORDER_DELIVERED = "tag_order_delivered";

    public static final String TAG_ORDER_RECEIVED = "tag_order_received";

    public static final String TAG_ORDER_MODIFY_AUDIT = "tag_order_modify_audits";

    /** 自动发放优惠卷 */
    public static final String TOPIC_ORDER_COUPON_AUTOMATIC_SEND = "topic_order_coupon_automatic_send";

    public static final String TAG_ORDER_COUPON_AUTOMATIC_CREATED = "tag_order_coupon_automatic_created";

    public static final String TAG_ORDER_COUPON_AUTOMATIC_DELIVERED = "tag_order_coupon_automatic_delivered";

    public static final String TOPIC_MEMBER_COUPON_AUTOMATIC_SEND = "topic_member_coupon_automatic_send";

    /**
     * 校验订单重复通知
     */
    public static final String TOPIC_ORDER_PAY_REPEAT_NOTIFY = "topic_order_pay_repeat_notify";

    /**
     * B2B订单支付通知
     */
    public static final String TOPIC_ORDER_PAY_NOTIFY = "topic_order_pay_notify";

    /**
     * 以岭健康管理中心订单支付通知消息
     */
    public static final String TOPIC_HMC_ORDER_PAY_NOTIFY = "topic_hmc_order_pay_notify";

    /**
     *企业付款记录
     */
    public static final String TOPIC_TRANSFER_PAY_NOTIFY = "topic_transfer_pay_notify";

    /**
     * 支付订单失效通知
     */
    public static final String TOPIC_PAY_ORDER_EXPIRED = "topic_pay_order_expired";

    /**
     * 订单延迟查询队列
     */
    public static final String TOPIC_PAY_ORDER_QUERY = "topic_pay_order_query";

    /**
     * 订单退款通知
     */
    public static final String TOPIC_ORDER_PAY_REFUND_NOTIFY = "topic_order_pay_refund_notify";
    /**
     * hmc系统退款申请
     */
    public static final String TOPIC_HMC_PAY_REFUND_NOTIFY = "topic_hmc_pay_refund_notify";

    /**
     * B2B结算单更新状态补偿
     */
    public static final String TOPIC_B2B_SETTLEMENT_STATUS_NOTIFY = "topic_b2b_settlement_status_notify";

    /**
     * 销售助手添加的客户审核通过通知
     */
    public static final String TOPIC_SALES_ADD_CUSTOMER_PASS_NOTIFY = "topic_sales_add_customer_pass_notify";
    /**
     * 销售助手队长邀请的新人注册成功后发送通知
     */
    public static final String TOPIC_SALES_INVITE_REGISTER_SUCCESS_NOTIFY = "topic_sales_invite_register_success_notify";
    /**
     * B2B有推广人的会员购买成功或修改推广人推送通知到佣金模块
     */
    public static final String TOPIC_BUY_B2B_MEMBER_PROMOTER_NOTIFY = "topic_buy_b2b_member_promoter_notify";


    /** 页面发货刷新库存数量 */
    public static final String TOPIC_GOODS_QTY_REFRESH_SEND = "topic_goods_qty_refresh";

    public static final String TAG_GOODS_QTY_REFRESH_SEND = "tag_goods_qty_refresh";

    /** 商品同步以后刷新op */
    public static final String TOPIC_GOODS_REFRESH="topic_goods_refresh";

    public static final String TAG_GOODS_REFRESH="tag_goods_refresh";

    /** 根据订阅库存刷新总库存 */
    public static final String TOPIC_QTY_REFRESH_BY_SUBSCRIPTION = "topic_qty_refresh_by_subscription";

    public static final String TAG_QTY_REFRESH_BY_SUBSCRIPTION = "tag_qty_refresh_by_subscription";

    /** 订单推送mq */
    public static final String TOPIC_ORDER_PUSH_ERP="topic_order_push_erp";

    public static final String TAG_ORDER_PUSH_ERP="tag_order_push_erp";

    /** 销售流向信息汇总mq */
    public static final String TOPIC_FLOW_SALE_SUMMARY="topic_flow_sale_summary";

    public static final String TAG_FLOW_SALE_SUMMARY="tag_flow_sale_summary";

    /** 流向数据索引刷新 */
    public static final String TOPIC_FLOW_INDEX_REFRESH="topic_flow_index_refresh";

    public static final String TAG_FLOW_INDEX_REFRESH="tag_flow_index_refresh";

    /** 流向数据清洗任务mq */
    public static final String TOPIC_FLOW_WASH_TASK="topic_flow_wash_task";

    public static final String TAG_FLOW_WASH_TASK="tag_flow_wash_task";

    /**非锁客户分类mq*/
    public static final String TOPIC_UNLOCK_CUSTOMER_CLASSIFICATION_TASK="topic_unlock_customer_classification_task";

    public static final String TAG_UNLOCK_CUSTOMER_CLASSIFICATION_TASK="tag_unlock_customer_classification_task";

    /**非锁客户对照匹配度任务*/
    public static final String TOPIC_UNLOCK_CUSTOMER_MATCHING_RATE_TASK="topic_unlock_customer_matching_rate_task";

    public static final String TAG_UNLOCK_CUSTOMER_MATCHING_RATE_TASK="tag_unlock_customer_matching_rate_task";

    public static final String TOPIC_UNLOCK_CUSTOMER_MATCHING_RATE_BATCH_TASK="topic_unlock_customer_matching_rate_batch_task";

    public static final String TAG_UNLOCK_CUSTOMER_MATCHING_RATE_BATCH_TASK="tag_unlock_customer_matching_rate_batch_task";

    /**非锁流向分配mq*/
    public static final String TOPIC_UNLOCK_FLOW_SALE_MATCH_TASK="topic_unlock_flow_sale_match_task";

    public static final String TAG_UNLOCK_FLOW_SALE_MATCH_TASK="tag_unlock_flow_sale_match_task";

    /**团购数据扣减流向处理mq*/
    public static final String TOPIC_FLOW_SALE_GB_SUBSTRACT_TASK="topic_flow_sale_gb_substract_task";

    public static final String TAG_FLOW_SALE_GB_SUBSTRACT_TASK="tag_flow_sale_gb_substract_task";

    /**团购数据匹配流向处理mq*/
    public static final String TOPIC_FLOW_SALE_GB_MATE_TASK="topic_flow_sale_gb_mate_task";

    public static final String TAG_FLOW_SALE_GB_MATE_TASK="tag_flow_sale_gb_mate_task";

    /**团购数据审核通过插入mq*/
    public static final String TOPIC_FLOW_SALE_GB_APPROVED_TASK="topic_flow_sale_gb_approved_task";

    public static final String TAG_FLOW_SALE_GB_APPROVED_TASK="tag_flow_sale_gb_approved_task";


    public static final String TOPIC_UNLOCK_FLOW_WASH_SALE_CLASSIFICATION="topic_unlock_flow_wash_sale_classification";

    public static final String TAG_UNLOCK_FLOW_WASH_SALE_CLASSIFICATION="tag_unlock_flow_wash_sale_classification";

    /** 商品映射任务mq */
    public static final String TOPIC_GOODS_MAPPING_MONTH_WASH="topic_goods_mapping_month_wash";

    public static final String TAG_GOODS_MAPPING_MONTH_WASH="tag_goods_mapping_month_wash";

    /** 客户映射任务mq */
    public static final String TOPIC_CUSTOMER_MAPPING_MONTH_WASH="topic_customer_mapping_month_wash";

    public static final String TAG_CUSTOMER_MAPPING_MONTH_WASH="tag_customer_mapping_month_wash";

    /** 客户映射任务mq */
    public static final String TOPIC_SUPPLIER_MAPPING_MONTH_WASH="topic_supplier_mapping_month_wash";

    public static final String TAG_SUPPLIER_MAPPING_MONTH_WASH="tag_supplier_mapping_month_wash";

    /** 流向数据每天汇总统计 */
    public static final String TOPIC_FLOW_BALANCE_STATISTICS="topic_flow_balance_statistics";

    public static final String TAG_FLOW_BALANCE_STATISTICS="tag_flow_balance_statistics";

    /** 销售流向信息汇总mq */
    public static final String TOPIC_FLOW_SALE_SUMMARY_SYNC="topic_flow_sale_summary_sync";

    public static final String TAG_FLOW_SALE_SUMMARY_SYNC="tag_flow_sale_summary_sync";

    /** 订单推送失败发送邮件mq */
    public static final String TOPIC_ORDER_PUSH_FAIL_MAIL="topic_order_push_fail_mail";

    public static final String TAG_ORDER_PUSH_FAIL_MAIL="tag_order_push_fail_mail";

    /** 计算销售助手任务进度 */
    public static final String TOPIC_SA_ORDER_TASK_RATE_SEND = "topic_as_order_task_rate_send";

    public static final String TAG_SA_ORDER_TASK_RATE_SEND= "tag_as_order_task_rate_send";
    /** 计算销售助手随货同行单任务进度 */
    public static final String TOPIC_SA_BILL_MATCH = "topic_sa_bill_match";
    public static final String TAG_SA_BILL_MATCH = "tag_sa_bill_match";

    /** 会员退款申请 */
    public static final String TOPIC_MEMBER_ORDER_REFUND = "topic_member_order_refund";

    /** 会员退款审核通过通知返利 */
    public static final String TOPIC_MEMBER_REFUND_PUSH_REBATE = "topic_member_refund_push_rebate";

    /** 会员退款审核通过通知佣金模块 */
    public static final String TOPIC_MEMBER_REFUND_PUSH_TASK = "topic_member_refund_push_task";

    /** 会员购买成功发送MQ通知返利侧 */
    public static final String TOPIC_BUY_B2B_MEMBER_SUCCESS = "topic_buy_b2b_member_success";

    /** 更新会员推广方或推广人发送MQ通知返利侧 */
    public static final String TOPIC_UPDATE_B2B_MEMBER_PROMOTER = "topic_update_b2b_member_promoter";

    /** 会员购买成功发送MQ通知策略满赠 */
    public static final String TOPIC_BUY_B2B_MEMBER_SUCCESS_STRATEGY_SEND = "topic_buy_b2b_member_success_strategy_send";

    /** B2B收货发送消息 */
    public static final String TOPIC_B2B_ORDER_RECEIVE_STATE_SEND = "topic_b2b_order_receive_state_send";

    public static final String TAG_B2B_ORDER_RECEIVE_STATE_SEND= "tag__b2b_order_receive_state_send";

    /** B2B订单签收赠送积分 */
    public static final String TOPIC_B2B_ORDER_GIVE_INTEGRAL_SEND = "topic_b2b_order_give_integral_send";

    /** 报表返利计算消息 */
    public static final String TOPIC_REPORT_REWARD_B2B_SEND = "topic_report_reward_b2b_send";

    public static final String TAG_REPORT_REWARD_B2B_SEND = "tag_report_reward_b2b_send";

    public static final String TOPIC_REPORT_REWARD_FLOW_SEND = "topic_report_reward_flow_send";

    public static final String TAG_REPORT_REWARD_FLOW_SEND = "tag_report_reward_flow_send";
    /**
     * 订单关闭通知
     */
    public static final String TOPIC_PAY_TRADE_CLOSE_NOTIFY = "topic_pay_trade_close_notify";

    /** HMC创建订单mq */
    public static final String TOPIC_HMC_CREATE_ORDER="topic_hmc_create_order";

    public static final String TAG_HMC_CREATE_ORDER="tag_hmc_create_order";

    /** HMC修改商品标签mq */
    public static final String TOPIC_HMC_UPDATE_GOODS_TAG="topic_hmc_update_goods_tag";

    public static final String TAG_HMC_UPDATE_GOODS_TAG="tag_hmc_update_goods_tag";

    /** HMC关注公众号延迟mq */
    public static final String TOPIC_HMC_GZH_SUBSCRIBE_DELAY="topic_hmc_gzh_subscribe_delay";

    public static final String TAG_HMC_GZH_SUBSCRIBE_DELAY="tag_hmc_gzh_subscribe_delay";

    /** HMC公众号互动延迟mq */
    public static final String TOPIC_HMC_GZH_INTERACT_DELAY="topic_hmc_gzh_interact_delay";

    public static final String TAG_HMC_GZH_INTERACT_DELAY="tag_hmc_gzh_interact_delay";

    /** HMC创建市场订单mq */
    public static final String TOPIC_HMC_CREATE_MARKET_ORDER="topic_hmc_create_market_order";

    /** HMC创建市场处方订单mq */
    public static final String TOPIC_HMC_CREATE_MARKET_PRE_ORDER="topic_hmc_create_market_pre_order";

    public static final String TAG_HMC_CREATE_MARKET_ORDER="tag_hmc_create_market_order";

    /** HMC完成订单mq */
    public static final String TOPIC_HMC_FINISH_ORDER="topic_hmc_finish_order";

    public static final String TAG_HMC_FINISH_ORDER="tag_hmc_finish_order";

    /** HMC关注公众号mq */
    public static final String TOPIC_HMC_GZH_SUBSCRIBE="topic_hmc_gzh_subscribe";

    public static final String TAG_HMC_GZH_SUBSCRIBE="tag_hmc_gzh_subscribe";

    /** HMC首次关注公众号mq */
    public static final String TOPIC_HMC_GZH_FIRST_SUBSCRIBE="topic_hmc_gzh_first_subscribe";

    public static final String TAG_HMC_GZH_FIRST_SUBSCRIBE="tag_hmc_gzh_first_subscribe";

    /** HMC注册小程序mq */
    public static final String TOPIC_HMC_REG_MINI_PROGRAM="topic_hmc_reg_mini_program";

    public static final String TAG_HMC_REG_MINI_PROGRAM="tag_hmc_reg_mini_program";

    /** erp流向报表消息 */
    public static final String TOPIC_ERP_FLOW_SALE_SETTLEMENT_SEND = "topic_erp_flow_sale_settlement_send";

    public static final String TAG_ERP_FLOW_SALE_SETTLEMENT_SEND = "tag_erp_flow_sale_settlement_send";

    public static final String TOPIC_ERP_FLOW_GOODS_RELATION_SETTLEMENT_SEND = "topic_erp_flow_goods_relation_settlement_send";

    public static final String TAG_ERP_FLOW_GOODS_RELATION_SETTLEMENT_SEND = "tag_erp_flow_goods_relation_settlement_send";

    public static final String TOPIC_ERP_FLOW_GOODS_RELATION_DATAFLOW = "topic_erp_flow_goods_relation_dataflow";

    public static final String TAG_ERP_FLOW_GOODS_RELATION_DATAFLOW  = "tag_erp_flow_goods_relation_dataflow";

    public static final String TOPIC_ERP_FLOW_GOODS_RELATION_SAVEORUPDATE = "topic_erp_flow_goods_relation_saveOrUpdate";

    public static final String TAG_ERP_FLOW_GOODS_RELATION_SAVEORUPDATE  = "tag_erp_flow_goods_relation_saveOrUpdate";

    /** 商品审核通知erp流向消息 **/
    public static final String TOPIC_GOODS_AUDIT_ERP_FLOW_SEND = "topic_goods_audit_erp_flow_send";

    public static final String TAG_GOODS_AUDIT_FLOW_SALE_SEND = "tag_goods_audit_flow_sale_send";

    /** 以岭品关系修改发送返利报表消息 **/
    public static final String TOPIC_FLOW_GOODS_RELATION_EDIT_TASK_SEND = "topic_flow_goods_relation_edit_task_send";
    public static final String TAG_FLOW_GOODS_RELATION_EDIT_TASK_SEND = "tag_flow_goods_relation_edit_task_send";

    /** 流向采购和销售线上不存在、备份中需删除的消息 **/
    public static final String TOPIC_ERP_PURCHASE_FLOW_DELETE_WITH_NO_DATA = "topic_erp_purchase_flow_delete_with_no_data";
    public static final String TAG_ERP_PURCHASE_FLOW_DELETE_WITH_NO_DATA = "tag_erp_purchase_flow_delete_with_no_data";

    public static final String TOPIC_ERP_SALE_FLOW_DELETE_WITH_NO_DATA = "topic_erp_sale_flow_delete_with_no_data";
    public static final String TAG_ERP_SALE_FLOW_DELETE_WITH_NO_DATA = "tag_erp_sale_flow_delete_with_no_data";

    /** CMS疑问处理第一次回复发送mq消息 */
    public static final String TOPIC_CMS_FIRST_REPLY_SEND="topic_cms_first_reply_send";

    public static final String TAG_CMS_FIRST_REPLY_SEND="tag_cms_first_reply_send";


    /** B2B自动收货信息*/
    public static final String TOPIC_B2B_ORDER_ACTIVE_RECEIVE = "topic_b2b_order_active_receive";

    public static final String TAG_B2B_ORDER_ACTIVE_RECEIVE = "tag_b2b_order_active_receive";

    /** 销售助手新增佣金*/
    public static final String TOPIC_SA_COMMISSIONS_SEND = "topic_sa_commissions_send";

    public static final String TAG_TOPIC_SA_COMMISSIONS_SEND = "tag_topic_sa_commissions_send";

    /** B2B预售订单尾款支付超时取消消息 */
    public static final String TOPIC_B2B_PRESALE_ORDER_CANCEL_SEND = "topic_b2b_presale_order_cancel_send";

    public static final String TAG_B2B_PRESALE_ORDER_CANCEL_SEND= "tag_b2b_presale_order_cancel_send";

    /** 客户下单成功mq*/
    public static final String TOPIC_CUSTOMER_CREATE_ORDER = "topic_customer_create_order";

    public static final String TAG_TOPIC_CUSTOMER_CREATE_ORDER = "tag_customer_create_order";

    /** 异步mq导入*/
    public static final String TOPIC_IMPORT_CREATE_EXCEL = "topic_import_create_excel";
    /**
     * 补传月流向
     */
    public static final String TOPIC_IMPORT_FIX_MONTH_FLOW = "topic_import_fix_month_flow";

    public static final String TAG_TOPIC_IMPORT_CREATE_EXCEL = "tag_topic_import_create_excel";
    /** 发送提交审核消息给工作流mq */
    public static final String TOPIC_SUBMIT_SEND_WORKFLOW = "topic_submit_send_workflow";
    /**驳回重新提交*/
    public static final String TOPIC_RESUBMIT_SEND_WORKFLOW = "topic_resubmit_send_workflow";
    public static final String TAG_ADD_AGENCY = "tag_add_agency";
    public static final String TAG_UPDATE_AGENCY = "tag_update_agency";
    public static final String TAG_EXTEND_CHANGE = "tag_extend_change";
    public static final String TAG_UNLOCK_AGENCY = "tag_unlock_agency";
    public static final String TAG_LOCK_AGENCY = "tag_lock_agency";
    public static final String TAG_AGENCY_RELATION_CHANGE = "tag_agency_relation_change";
    public static final String TAG_DISPLACE_GOODS_CHANGE = "tag_displace_goods_change";
    public static final String TAG_SALES_GOODS_CHANGE = "tag_sales_goods_change";
    public static final String TAG_MANOR_CHANGE = "tag_manor_change";
    public static final String TAG_HOSPITAL_DRUGSTORE_RELATION_ADD = "tag_hospital_drugstore_relation_add";
    public static final String TAG_FIX_MONTH_FLOW = "tag_fix_month_flow";

    /**变更表单状态发送消息给sjms*/
    public static final String TOPIC_CHANGE_FORM_APPROVE = "topic_change_form_approve";
    public static final String TAG_CHANGE_FORM_APPROVE = "tag_change_form_approve";
    public static final String TOPIC_CHANGE_FORM_SUBMIT = "topic_change_form_submit";

    public static final String TOPIC_CHANGE_FORM_REJECT = "topic_change_form_reject";
    public static final String TAG_CHANGE_FORM_REJECT = "tag_change_form_reject";

    public static final String TOPIC_CHANGE_FORM_RESUBMIT = "topic_change_form_resubmit";
    public static final String TAG_CHANGE_FORM_RESUBMIT = "tag_change_form_resubmit";

    /** 团购提报发送消息给工作流mq */
    public static final String TOPIC_GB_SUBMIT_SEND_WORKFLOW = "topic_gb_submit_send_workflow";
    public static final String TAG_GB_SUBMIT_SEND_WORKFLOW = "tag_gb_submit_send_workflow";
    /**自动审批mq*/
    public static final String TOPIC_AUTO_APPROVE = "topic_auto_approve";

    /** 团购驳回重新提交发送消息给工作流mq */
    public static final String TOPIC_GB_RESUBMIT_SEND_WORKFLOW = "topic_gb_resubmit_send_workflow";
    public static final String TAG_GB_RESUBMIT_SEND_WORKFLOW = "tag_gb_resubmit_send_workflow";
    /**
     * OA 待办
     */
    public static final String TOPIC_OA_TODO_RECV = "topic_oa_todo_recv";
    public static final String TAG_OA_TODO_RECV = "tag_oa_todo_recv";
    public static final String TOPIC_OA_TODO_DONE = "topic_oa_todo_done";
    public static final String TAG_OA_TODO_DONE = "tag_oa_todo_done";
    public static final String TOPIC_OA_TODO_OVER = "topic_oa_todo_over";
    public static final String TAG_OA_TODO_OVER = "tag_oa_todo_recv_over";

    /** 团购取消发送消息给工作流mq */
    public static final String TOPIC_GB_CANCEL_SEND_WORKFLOW = "topic_gb_cancel_send_workflow";
    public static final String TAG_GB_CANCEL_SEND_WORKFLOW = "tag_gb_cancel_send_workflow";

    /** 团购取消驳回重新提交发送消息给工作流mq */
    public static final String TOPIC_GB_CANCEL_RESUBMIT_SEND_WORKFLOW = "topic_gb_cancel_resubmit_send_workflow";
    public static final String TAG_GB_CANCEL_RESUBMIT_SEND_WORKFLOW = "tag_gb_cancel_resubmit_send_workflow";

    /** 大运河数拓 */
    public static final String DYH_ST_DEPARTMENT_CODE  ="DYHST";

    /** 大运河分销 */
    public static final String DYH_FX_DEPARTMENT_CODE  ="DYHFX";

    /** 万州国际 */
    public static final String WZ_DEPARTMENT_CODE  ="WZGJ";


    /**
     * 预售订单在线支付自动取前,短信提醒
     */
    public static final String TOPIC_ORDER_CANCEL_SMS_NOTIFY = "topic_order_cancel_sms_notify";

    /**
     * 退款申请单自动审核通知
     */
    public static final String TOPIC_REFUND_ORDER_AUDIT_NOTIFY = "topic_refund_order_audit_notify";

    /**
     * 清洗任务清洗完成通知
     */
    public static final String TOPIC_WASH_TASK_FINISH_NOTIFY = "topic_Wash_task_finish_notify";

    /**
     * 清洗任务清洗完成tag
     */
    public static final String TAG_WASH_TASK_FINISH_NOTIFY = "tag_Wash_task_finish_notify";

    /**
     * 清洗任务变更通知
     */
    public static final String TOPIC_WASH_TASK_CHANGE_NOTIFY = "topic_Wash_task_change_notify";

    /**
     * 清洗任务变更tag
     */
    public static final String TAG_WASH_TASK_CHANGE_NOTIFY = "tag_Wash_task_change_notify";

    /**
     * 团购提报数据复核通过发送mq给团购流向业务
     */
    public static final String TOPIC_FLOW_SALE_GB_REVIEW_APPROVED_TASK="topic_flow_sale_gb_review_approved_task";
    public static final String TAG_FLOW_SALE_GB_REVIEW_APPROVED_TASK="tag_flow_sale_gb_review_approved_task";


    /**
     * 流向业务日期通知
     */
    public static final String TOPIC_LASTEST_FLOW_DATE_NOTIFY = "topic_lastest_flow_date_notify";
    public static final String TAG_LASTEST_FLOW_DATE_NOTIFY = "tag_lastest_flow_date_notify";

    /**
     * 清洗任务清洗完成统计通知
     */
    public static final String TOPIC_WASH_TASK_FINISH_SUM_NOTIFY = "topic_Wash_task_finish_sum_notify";

    /**
     * 团购流向生成通知
     */
    public static final String TOPIC_GB_FLOW_NOTIFY = "topic_gb_flow_notify";
    public static final String TAG_GB_FLOW_NOTIFY = "tag_gb_flow_notify";

    /**
     * 清洗任务清洗完成统计通知tag
     */
    public static final String TAG_WASH_TASK_FINISH_SUM_NOTIFY = "tag_Wash_task_finish_sum_notify";

    /**
     * 三者关系备份以后更新变更表内容
     */
    public static final String TOPIC_UPDATE_CRM_RELATIONSHIP = "topic_update_crm_relationship";

    /**
     * 三者关系备份以后更新变更表内容 tag
     */
    public static final String TAG_UPDATE_CRM_RELATIONSHIP = "tag_update_crm_relationship";


    /**
     * 修改流向销售合并报表中关于部门信息字段
     */
    public static final String TOPIC_UPDATE_SALE_REPORT_CRM_RELATIONSHIP = "topic_update_sale_report_crm_relationship";

    public static final String TAG_UPDATE_SALE_REPORT_CRM_RELATIONSHIP = "tag_update_sale_report_crm_relationship";
    /**
     * 生成销售指标模板
     */
    public static final String TOPIC_GENERATE_CRM_SALE_DEPARTMENT_TARGET_CONFIG = "topic_generate_crm_sale_department_target_config";

    public static final String TAG_GENERATE_CRM_SALE_DEPARTMENT_TARGET_CONFIG = "tag_generate_crm_sale_department_target_config";
    /**
     * 生成销售指标分解明细模板
     */
    public  static  final String TOPIC_GEN_SALE_TARGET_TEMPLATE = "topic_gen_sale_target_template";

    public  static  final String TAG_GEN_SALE_TARGET_TEMPLATE = "tag_gen_sale_target_template";

    /**
     * 团购费用申请tag
     */
    public static final String TAG_GB_FEE_APPLICATION_SEND_WORKFLOW = "tag_gb_fee_application_send_workflow";

    /** ======================================== MQ Topic & Tag - end ================================================== */

    /** 图片文件后缀 */
    public static final String FILE_SUFFIX= ".png";

    /**
     * 以岭互联网医院项目成功返回码
     */
    public static final int HOSPITAL_SUCCESS_CODE = 1;

    /**
     * 抽奖活动海报背景图
     */
    public static String LOTTERY_ACTIVITY_BACK = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/lottery_activity_back.png";

    /**
     * 是
     */
    public static final Integer IS_YES = 1;

    /**
     * 否
     */
    public static final Integer IS_NO = 2;
}
