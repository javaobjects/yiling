package com.yiling.erp.client.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.yiling.erp.client.common.MidTableTemplateSql;

import cn.hutool.core.map.MapUtil;

public class ErpClientMidTable {
    public static  String              fileMidName = "createMidTableTemplate.xml";
    public static  ErpClientMidTable   instance;
    public static  MidTableTemplateSql em;
    private static String              targetPlace = ErpClientMidTable.class.getClassLoader().getResource(fileMidName).getPath();

    public static synchronized ErpClientMidTable getInstance() {
        if (instance == null) {
            instance = new ErpClientMidTable();
            em = instance.readSqlSet();
        }
        return instance;
    }

    private MidTableTemplateSql readSqlSet() {
        MidTableTemplateSql mts = new MidTableTemplateSql();
        try {
            String xml = Utils.readFileByLines(targetPlace);
            if ((xml != null) && (!xml.equals(""))) {
                JAXBContext context = JAXBContext.newInstance(new Class[]{MidTableTemplateSql.class});
                Unmarshaller unmarshaller = context.createUnmarshaller();
                mts = (MidTableTemplateSql) unmarshaller.unmarshal(new StringReader(xml));
            }
        } catch (Exception e) {
            new ErpException("读取中间表建表sql模板出错", e);
        }
        return mts;
    }

    public static Map<String, String> getOrderColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000"))) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("order_no", "varchar");
//            map.put("enterprise_inner_code", "varchar");
//            map.put("enterprise_name", "varchar");
//            map.put("order_date", "datetime");
//            map.put("seller_inner_code", "varchar");
//            map.put("sale_inner_code", "varchar");
//            map.put("receive_mobile", "varchar");
//            map.put("receive_user", "varchar");
//            map.put("receive_address", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "int");
//            map.put("remark", "varchar");
//            map.put("payment_method", "int");
//            map.put("payment_status", "int");
//            map.put("seller_eid", "int");
//        } else if ("oracle".equalsIgnoreCase(type)) {
//            map.put("id", "number");
//            map.put("order_id", "number");
//            map.put("order_no", "varchar");
//            map.put("enterprise_code", "varchar");
//            map.put("enterprise_name", "varchar");
//            map.put("order_date", "datetime");
//            map.put("seller_inner_code", "varchar");
//            map.put("sale_inner_code", "varchar");
//            map.put("receive_mobile", "varchar");
//            map.put("receive_user", "varchar");
//            map.put("receive_address", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "number");
//            map.put("remark", "varchar");
//            map.put("seller_eid", "number");
//        } else if ("mysql".equalsIgnoreCase(type)) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("order_no", "varchar");
//            map.put("enterprise_inner_code", "varchar");
//            map.put("enterprise_name", "varchar");
//            map.put("order_date", "datetime");
//            map.put("seller_inner_code", "varchar");
//            map.put("sale_inner_code", "varchar");
//            map.put("receive_mobile", "varchar");
//            map.put("receive_user", "varchar");
//            map.put("receive_address", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "int");
//            map.put("remark", "varchar");
//            map.put("seller_eid", "int");
//        }
        return map;
    }

    public static Map<String, String> getErpBillColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000"))) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("eas_delivery_number", "varchar");
//        } else if ("oracle".equalsIgnoreCase(type)) {
//            map.put("id", "number");
//            map.put("order_id", "number");
//            map.put("eas_delivery_number", "varchar");
//        } else if ("mysql".equalsIgnoreCase(type)) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("eas_delivery_number", "varchar");
//        }
        return map;
    }

    public static Map<String, String> getOrderDetailColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000"))) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("order_detail_id", "int");
//            map.put("order_no", "varchar");
//            map.put("enterprise_inner_code", "varchar");
//            map.put("seller_inner_code", "varchar");
//            map.put("goods_in_sn", "varchar");
//            map.put("goods_name", "varchar");
//            map.put("buy_number", "int");
//            map.put("goods_price", "int");
//            map.put("goods_amount", "int");
//            map.put("create_time", "datetime");
//        } else if ("oracle".equalsIgnoreCase(type)) {
//            map.put("id", "number");
//            map.put("order_id", "number");
//            map.put("order_no", "varchar");
//            map.put("enterprise_code", "varchar");
//            map.put("seller_inner_code", "varchar");
//            map.put("goods_in_sn", "varchar");
//            map.put("goods_name", "varchar");
//            map.put("buy_number", "number");
//            map.put("goods_price", "number");
//            map.put("goods_amount", "number");
//            map.put("create_time", "datetime");
//        } else if ("mysql".equalsIgnoreCase(type)) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("order_detail_id", "int");
//            map.put("order_no", "varchar");
//            map.put("enterprise_inner_code", "varchar");
//            map.put("seller_inner_code", "varchar");
//            map.put("goods_in_sn", "varchar");
//            map.put("goods_name", "varchar");
//            map.put("buy_number", "int");
//            map.put("goods_price", "int");
//            map.put("goods_amount", "int");
//            map.put("create_time", "datetime");
//        }
        return map;
    }


    public static Map<String, String> getOrderBillColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ("mysql".equalsIgnoreCase(type)) {
//            map.put("id", "int");
//            map.put("order_bill_id", "int");
//            map.put("order_id", "int");
//            map.put("order_detail_id", "int");
//            map.put("goods_in_sn", "varchar");
//            map.put("discount_rate", "decimal");
//            map.put("discount_amount", "decimal");
//            map.put("eas_delivery_number", "varchar");
//            map.put("eas_bill_number", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "int");
//            map.put("ticket_type", "varchar");
//        }
        return map;
    }


    public static Map<String, String> getOrderReturnColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000"))) {
//            map.put("id", "int");
//            map.put("order_return_id", "int");
//            map.put("erp_order_no", "varchar");
//            map.put("order_id", "int");
//            map.put("order_detail_id", "int");
//            map.put("return_number", "int");
//            map.put("batch_no", "varchar");
//            map.put("goods_in_sn", "varchar");
//            map.put("enterprise_inner_code", "varchar");
//            map.put("seller_inner_code", "varchar");
//            map.put("sale_inner_code", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "int");
//            map.put("eas_delivery_number", "varchar");
//        }else if ("mysql".equalsIgnoreCase(type)) {
//            map.put("id", "int");
//            map.put("order_return_id", "int");
//            map.put("erp_order_no", "varchar");
//            map.put("order_id", "int");
//            map.put("order_detail_id", "int");
//            map.put("return_number", "int");
//            map.put("batch_no", "varchar");
//            map.put("goods_in_sn", "varchar");
//            map.put("enterprise_inner_code", "varchar");
//            map.put("seller_inner_code", "varchar");
//            map.put("sale_inner_code", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "int");
//            map.put("eas_delivery_number", "varchar");
//        }
        return map;
    }

    public static Map<String, String> getReturnColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000"))) {
//            map.put("id", "int");
//            map.put("return_id", "int");
//            map.put("seller_eid", "int");
//        }else if ("mysql".equalsIgnoreCase(type)) {
//            map.put("id", "int");
//            map.put("return_id", "int");
//            map.put("seller_eid", "int");
//        }
        return map;
    }

    public static Map<String, String> getAgreementColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ("mysql".equalsIgnoreCase(type)) {
//            map.put("apply_id", "int");
//            map.put("apply_code", "varchar");
//            map.put("seller_code", "varchar");
//            map.put("seller_name", "varchar");
//            map.put("year", "varchar");
//            map.put("month", "varchar");
//            map.put("goods_name", "varchar");
//            map.put("entry_code", "varchar");
//            map.put("owner_code", "varchar");
//            map.put("entry_name", "varchar");
//            map.put("owner_name", "varchar");
//            map.put("total_amount", "decimal");
//            map.put("agreement_name", "varchar");
//            map.put("agreement_content", "varchar");
//            map.put("province_name", "varchar");
//            map.put("rebate_category", "varchar");
//            map.put("cost_subject", "varchar");
//            map.put("cost_dept", "varchar");
//            map.put("execute_dept", "varchar");
//            map.put("reply_code", "varchar");
//            map.put("create_time", "datetime");
//            map.put("create_user", "int");
//            map.put("create_user_code", "varchar");
//            map.put("entry_time", "datetime");
//        }
        return map;
    }

    public static Map<String, String> getAgreementDetailColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ("mysql".equalsIgnoreCase(type)) {
//            map.put("apply_id", "int");
//            map.put("apply_code", "varchar");
//            map.put("detail_type", "int");
//            map.put("agreement_id", "int");
//            map.put("order_count", "int");
//            map.put("amount", "decimal");
//            map.put("seller_eid", "int");
//            map.put("seller_name", "varchar");
//            map.put("seller_code", "varchar");
//            map.put("rebate_category", "varchar");
//            map.put("cost_subject", "varchar");
//            map.put("cost_dept", "varchar");
//            map.put("execute_dept", "varchar");
//            map.put("reply_code", "varchar");
//            map.put("describe", "varchar");
//            map.put("create_time", "datetime");
//            map.put("update_time", "datetime");
//            map.put("create_user", "int");
//            map.put("update_user", "int");
//            map.put("remark", "varchar");
//        }
        return map;
    }

    public static Map<String, String> getOrderPurchase(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000"))) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("order_no", "varchar");
//            map.put("seller_inner_code", "varchar");
//            map.put("seller_name", "varchar");
//            map.put("order_date", "datetime");
//            map.put("receive_mobile", "varchar");
//            map.put("receive_user", "varchar");
//            map.put("receive_address", "varchar");
//            map.put("payment_method", "int");
//            map.put("payment_status", "int");
//            map.put("remark", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "int");
//            map.put("status_time", "datetime");
//            map.put("seller_eid", "int");
//        } else if ("oracle".equalsIgnoreCase(type)) {
//            map.put("id", "number");
//            map.put("order_id", "number");
//            map.put("order_no", "varchar");
//            map.put("seller_inner_code", "varchar");
//            map.put("seller_name", "varchar");
//            map.put("order_date", "datetime");
//            map.put("receive_mobile", "varchar");
//            map.put("receive_user", "varchar");
//            map.put("receive_address", "varchar");
//            map.put("payment_method", "number");
//            map.put("payment_status", "number");
//            map.put("remark", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "number");
//            map.put("status_time", "datetime");
//            map.put("seller_eid", "number");
//        } else if ("mysql".equalsIgnoreCase(type)) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("order_no", "varchar");
//            map.put("seller_inner_code", "varchar");
//            map.put("seller_name", "varchar");
//            map.put("order_date", "datetime");
//            map.put("receive_mobile", "varchar");
//            map.put("receive_user", "varchar");
//            map.put("receive_address", "varchar");
//            map.put("payment_method", "int");
//            map.put("payment_status", "int");
//            map.put("remark", "varchar");
//            map.put("create_time", "datetime");
//            map.put("status", "int");
//            map.put("status_time", "datetime");
//            map.put("seller_eid", "int");
//        }
        return map;
    }

    public static Map<String, String> getOrderPurchaseDetailColumns(String type, String tableName) {
        Map<String, String> map = InitErpConfig.tableColumnMap.get(tableName);
        if(MapUtil.isEmpty(map)){
            return new HashMap<>();
        }
//        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000"))) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("order_no", "varchar");
//            map.put("order_detail_id", "int");
//            map.put("seller_inner_code", "varchar");
//            map.put("goods_in_sn", "varchar");
//            map.put("goods_name", "varchar");
//            map.put("buy_number", "int");
//            map.put("goods_price", "int");
//            map.put("goods_amount", "int");
//            map.put("create_time", "datetime");
//        } else if ("oracle".equalsIgnoreCase(type)) {
//            map.put("id", "number");
//            map.put("order_id", "number");
//            map.put("order_no", "varchar");
//            map.put("order_detail_id", "number");
//            map.put("seller_inner_code", "varchar");
//            map.put("goods_in_sn", "varchar");
//            map.put("goods_name", "varchar");
//            map.put("buy_number", "number");
//            map.put("goods_price", "number");
//            map.put("goods_amount", "number");
//            map.put("create_time", "datetime");
//        } else if ("mysql".equalsIgnoreCase(type)) {
//            map.put("id", "int");
//            map.put("order_id", "int");
//            map.put("order_no", "varchar");
//            map.put("order_detail_id", "int");
//            map.put("seller_inner_code", "varchar");
//            map.put("goods_in_sn", "varchar");
//            map.put("goods_name", "varchar");
//            map.put("buy_number", "int");
//            map.put("goods_price", "int");
//            map.put("goods_amount", "int");
//            map.put("create_time", "datetime");
//        }
        return map;
    }

    public static Map<String, String> getOrderPurchaseSend(String type) {
        Map<String, String> map = new HashMap<>();
        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000")) || (type.equalsIgnoreCase("ODBC")) || (type.equalsIgnoreCase("ODBC-DBF"))) {
            map.put("id", "int");
            map.put("order_id", "int");
            map.put("order_no", "varchar");
            map.put("seller_inner_code", "varchar");
            map.put("seller_name", "varchar");
            map.put("order_date", "datetime");
            map.put("receive_mobile", "varchar");
            map.put("receive_user", "varchar");
            map.put("receive_address", "varchar");
            map.put("payment_method", "int");
            map.put("payment_status", "int");
            map.put("remark", "varchar");
            map.put("create_time", "datetime");
            map.put("status", "int");
            map.put("status_time", "datetime");
        } else if ("oracle".equalsIgnoreCase(type)) {
            map.put("id", "number");
            map.put("order_id", "number");
            map.put("order_no", "varchar");
            map.put("seller_inner_code", "varchar");
            map.put("seller_name", "varchar");
            map.put("order_date", "datetime");
            map.put("receive_mobile", "varchar");
            map.put("receive_user", "varchar");
            map.put("receive_address", "varchar");
            map.put("payment_method", "number");
            map.put("payment_status", "number");
            map.put("remark", "varchar");
            map.put("create_time", "datetime");
            map.put("status", "number");
            map.put("status_time", "datetime");
        } else if ("mysql".equalsIgnoreCase(type)) {
            map.put("id", "int");
            map.put("order_id", "int");
            map.put("order_no", "varchar");
            map.put("seller_inner_code", "varchar");
            map.put("seller_name", "varchar");
            map.put("order_date", "datetime");
            map.put("receive_mobile", "varchar");
            map.put("receive_user", "varchar");
            map.put("receive_address", "varchar");
            map.put("payment_method", "int");
            map.put("payment_status", "int");
            map.put("remark", "varchar");
            map.put("create_time", "datetime");
            map.put("status", "int");
            map.put("status_time", "datetime");
        }
        return map;
    }

    public static Map<String, String> getOrderPurchaseSendDetailColumns(String type) {
        Map<String, String> map = new HashMap<>();
        if ((type.equalsIgnoreCase("SQL Server")) || (type.equalsIgnoreCase("SQL Server2000")) || (type.equalsIgnoreCase("ODBC")) || (type.equalsIgnoreCase("ODBC-DBF"))) {
            map.put("id", "int");
            map.put("order_id", "int");
            map.put("order_no", "varchar");
            map.put("order_detail_id", "int");
            map.put("seller_inner_code", "varchar");
            map.put("goods_in_sn", "varchar");
            map.put("goods_name", "varchar");
            map.put("buy_number", "int");
            map.put("goods_price", "int");
            map.put("goods_amount", "int");
            map.put("create_time", "datetime");
        } else if ("oracle".equalsIgnoreCase(type)) {
            map.put("id", "number");
            map.put("order_id", "number");
            map.put("order_no", "varchar");
            map.put("order_detail_id", "number");
            map.put("seller_inner_code", "varchar");
            map.put("goods_in_sn", "varchar");
            map.put("goods_name", "varchar");
            map.put("buy_number", "number");
            map.put("goods_price", "number");
            map.put("goods_amount", "number");
            map.put("create_time", "datetime");
        } else if ("mysql".equalsIgnoreCase(type)) {
            map.put("id", "int");
            map.put("order_id", "int");
            map.put("order_no", "varchar");
            map.put("order_detail_id", "int");
            map.put("seller_inner_code", "varchar");
            map.put("goods_in_sn", "varchar");
            map.put("goods_name", "varchar");
            map.put("buy_number", "int");
            map.put("goods_price", "int");
            map.put("goods_amount", "int");
            map.put("create_time", "datetime");
        }
        return map;
    }
}