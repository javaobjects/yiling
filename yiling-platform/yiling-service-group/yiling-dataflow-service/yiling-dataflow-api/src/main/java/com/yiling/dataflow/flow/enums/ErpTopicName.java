package com.yiling.dataflow.flow.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuan
 */

public enum ErpTopicName {
    ErpHeart("99999999","erp_heart","","心跳信息同步"),
    ErpLog("88888888","erp_log","","日志信息同步"),

    ErpCustomer("10000001","erp_customer","inner_code","客户信息同步"),
    ErpGoods("10000002","erp_goods","in_sn","商品信息同步"),
    ErpGoodsBatch("10000003","erp_goods_batch","gb_id_no","库存信息同步"),
    ErpOrderSend("10000004","erp_order_send","osi_id","发货单同步"),
    ErpBillInvoice("10000005","","","应收单与发票关联接口"),
    ErpCancelInvoice("10000006","","","发票作废接口"),
    ErpStatement("10000007","erp_settlement","","结算单接口"),
    ErpDeleteBill("10000008","","","删除应收单通知接口"),
    ErpGoodsPrice("10000009","erp_goods_customer_price","gcp_id_no","客户定价接口"),
    ErpGroupPrice("10000010","erp_goods_group_price","ggp_id_no","客户分组定价接口"),
    ErpOrderPurchaseDelivery("10000011","erp_order_purchase_delivery","delivery_no","采购入库单同步"),

    ErpOrder("20000001","erp_order","","订单同步"),
    ErpOrderReturn("20000002","erp_order_return","","订单回写同步"),
    ErpOrderErpSn("20000003","erp_order_erpsn","","EAS订单回写ErpSn"),
    ErpOrderBack("20000004","erp_order_back","","退货单同步"),
    ErpOrderBackReturn("20000005","erp_order_back_return","","退货单回写同步"),
    ErpOrderBackErpSn("20000006","erp_order_back_erpsn","","EAS退货单回写ErpSn"),
    ErpOrderBill("20000007","erp_order_bill","order_bill_id","开票申请同步"),
    ErpOrderBillReturn("20000008","erp_order_bill_return","order_bill_id","开票申请同步回写"),
    ErpOrderBillErpSn("20000009","erp_order_bill_erpsn","order_bill_id","EAS开票申请同步回写"),

    ErpAgreementApply("20000010","agreement_apply","","协议返利申请查询"),
    ErpAgreementApplyReturn("20000011","agreement_apply_return","","协议返利申请回写"),

    ErpDeleteData("20000012","erp_delete_data","","删除数据查询"),
    ErpDeleteDataReturn("20000013","erp_delete_data_return","","删除数据回写"),
    ErpOrderPurchase("20000014","erp_order_purchase","order_id","采购订单同步"),
    ErpOrderPurchaseReturn("20000015","erp_order_purchase_return","","采购订单同步回写"),
    ErpOrderPurchaseSend("20000016","erp_order_purchase_send","delivery_number","采购发货单同步"),
    ErpOrderPurchaseSendReturn("20000017","erp_order_purchase_send_return","","采购发货单同步回写"),


    SyncConfig("30000001","sync_config","","配置信息同步"),
    ErpToolVersion("30000002","erp_tool_version","",""),
    ErpRuningLog("30000003","erp_runing_log","",""),
    ErpCommand("30000004","erp_command","","命令执行同步"),
    ErpCommandBack("30000005","erp_command_back","","命令执行同步回写"),
    ErpVerifyData("30000006","erp_verify_data","","数据校验任务"),
    ErpEasOrderNo("30000007","erp_eas_orderno","","eas销售单号回写"),
    ErpEasNotify("30000008","erp_eas_notify","","eas通知程序同步"),
    ErpExecuteConfig("30000009","erp_execute_config","","erp远程执行配置信息"),
    RedisCommand("30000010","redis_command","","Redis命令执行同步"),
    SqlSelect("30000011","sql_select","","sql查询命令"),
    SqlSaveTask("30000012","sql_save_task","","sql保存命令"),
    deleteCache("30000013","delete_cache","","删除缓存命令"),
    SqlSelectTask("30000014","sql_select_task","","sql任务查询"),
    RedisCommandGet("30000015","redis_command_get","","erp命令心跳"),
    RedisCommandUpdate("30000016","redis_command_back","","redis任务命令回写"),
    SqlExecute("30000017","sql_execute","","sql执行命令"),
    upgradeCommand("30000018","upgrade_command","","版本升级"),
    upgradeLog("30000019","upgrade_log","","版本升级日志"),

    ErpFlowControl("40000005","erp_flow_control","","流向任务"),
    ErpPurchaseFlow("40000006","erp_purchase_flow","","采购流向"),
    ErpSaleFlow("40000007","erp_sale_flow","","销售流向"),
    ErpGoodsBatchFlow("40000008","erp_goods_batch_flow","gb_id_no","库存流向信息同步"),
    ErpPurchaseFlowData("40000009","erp_purchase_flow_interface","","采购流向接口同步"),
    ErpSaleFlowData("40000010","erp_sale_flow_interface","","销售流向接口同步"),
    ErpGoodsBatchFlowData("40000011","erp_goods_batch_flow_interface","","库存流向接口同步"),

    erpOrderAttachment("50000001","erp_order_attachment","","订单销售合同"),
    ;

    private String method;
    private String topicName;
    private String erpKey;
    private String topicNameDesc;

    private static final Map<String, String> map;

    static {
        map = new HashMap<String, String>();
        for (ErpTopicName type : values()) {
            map.put(type.getMethod(), type.getTopicName());
        }
    }

    public static Map<String, String> getMap() {
        return map;
    }

    ErpTopicName(String method, String topicName, String erpKey, String topicNameDesc) {
        this.method = method;
        this.topicName = topicName;
        this.erpKey=erpKey;
        this.topicNameDesc=topicNameDesc;
    }

    public static ErpTopicName getFromCode(String code) {
        for(ErpTopicName e: ErpTopicName.values()) {
            if(e.getMethod().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getErpKey() {
        return erpKey;
    }

    public void setErpKey(String erpKey) {
        this.erpKey = erpKey;
    }

    public String getTopicNameDesc() {
        return topicNameDesc;
    }

    public void setTopicNameDesc(String topicNameDesc) {
        this.topicNameDesc = topicNameDesc;
    }
}
