package com.yiling.erp.client.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.map.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.dao.PushConfigDao;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.ErpClientMidTable;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.ReadProperties;
import com.yiling.erp.client.util.StringUtil;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.PushConfig;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.erp.client.util.PopRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service("clientOrderReturnService")
public class ClientOrderReturnServiceImpl implements SyncTaskService {

    @Autowired
    private PushConfigDao pushConfigDao;

    @Autowired
    private InitErpConfig initErpConfig;

    @Override
    public void syncData() {
        try {
            log.info("[退货单同步] 任务开始");
            int orderSize = Integer.valueOf(ReadProperties.getProperties("orderSize")).intValue();
            PushConfig pushConfig = null;
            //查询推送订单数据源配置
            List<PushConfig> list = pushConfigDao.executeQueryPushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from push_config");
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            pushConfig = list.get(0);
            SysConfig sysConfig = InitErpConfig.toSysConfig(pushConfig);
            if(MapUtil.isEmpty(InitErpConfig.tableColumnMap)){
                DataBaseConnection.getInstance().initTableInfo(sysConfig, InitErpConfig.tableColumnMap);
            }
            //获取需要推送的订单接口
            PopRequest pr = new PopRequest();
            Map<String, String> headParams = pr.generateHeadMap(ErpTopicName.ErpOrderBack.getMethod(), initErpConfig.getSysConfig().getKey());
            String returnValue = pr.getPost(initErpConfig.getSysConfig().getUrlPath(), "{pageSize:" + orderSize + "}", headParams, initErpConfig.getSysConfig().getSecret());
            //插入到erp系统里
            log.debug("[退货单同步] 服务端返回信息"+returnValue);
            if ((returnValue != null) && (!returnValue.equals(""))) {
                Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
                Integer code = apiResponse.getCode();
                if (200 != code) {
                    return;
                }
                String returnObject = (String) apiResponse.getData();
                List<Map<String, String>> mapList = insertOrder(sysConfig, returnObject);

                if (mapList != null) {
                    notificationOrderIssuedFailure(initErpConfig.getSysConfig(), mapList);
                }
            }
        } catch (Exception e) {
            log.error("订单信息同步到服务器报错", e);
        }
    }

    private List<Map<String, String>> insertOrder(SysConfig sysConfig, String returnObject) {
        JSONArray jsonArray = JSONArray.parseArray(returnObject);
        if(jsonArray==null||jsonArray.isEmpty()){
            return null;
        }
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            Map<String, String> orderDetailList = ErpClientMidTable.getOrderReturnColumns(sysConfig.getDbType(), "yiling_order_return");
            Map<String, String> orderList = ErpClientMidTable.getReturnColumns(sysConfig.getDbType(), "yiling_return");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if (StringUtils.isEmpty(object.getString("return_id"))) {
                    return null;
                }
                String order_id = object.getString("return_id");
                String eas_delivery_number = object.getString("eas_delivery_number");
                String repeatProcessSql = "select id from yiling_return where return_id=" + order_id;
                String afterProcessMasterSql = "insert into yiling_return(";

                String vs = "values(";
                for (String str : object.keySet()) {
                    if (orderList.keySet().contains(str)) {
                        afterProcessMasterSql += str + ",";
                        if (orderList.get(str).toUpperCase().equals("VARCHAR") || orderList.get(str).toUpperCase().equals("VARCHAR2")) {
                            vs += "'" + object.get(str) + "',";
                        } else if (orderList.get(str).toUpperCase().equals("TIMESTAMP") || orderList.get(str).toUpperCase().equals("DATETIME") || orderList.get(str).toUpperCase().equals("DATE")) {
                            vs += "'" + object.get(str) + "',";
                        } else {
                            vs += object.get(str) + ",";
                        }
                    }
                }
                afterProcessMasterSql = afterProcessMasterSql.substring(0, afterProcessMasterSql.length() - 1);
                vs = vs.substring(0, vs.length() - 1);
                afterProcessMasterSql = afterProcessMasterSql.replaceAll(",\\s*$", "");
                vs = vs.replaceAll(",\\s*$", "");
                afterProcessMasterSql += ")" + vs + ");";


                JSONArray orderDetailObject = object.getJSONArray("order_return_list");
                StringBuffer orderItemSql=new StringBuffer("");

                for (int j = 0; j < orderDetailObject.size(); j++) {
                    JSONObject orderDetail = orderDetailObject.getJSONObject(j);
                    String vsD = "values(";
                    String orderItemSqlList = "insert into yiling_order_return(";
                    for (String orderDetailStr : orderDetail.keySet()) {
                        if (orderDetailList.keySet().contains(orderDetailStr)) {
                            orderItemSqlList += orderDetailStr + ",";
                            if (orderDetailList.get(orderDetailStr).toUpperCase().equals("VARCHAR") || orderDetailList.get(orderDetailStr).toUpperCase().equals("VARCHAR2")) {
                                vsD += "'" + orderDetail.get(orderDetailStr) + "',";
                            }  else if (orderDetailList.get(orderDetailStr).toUpperCase().equals("TIMESTAMP") || orderDetailList.get(orderDetailStr).toUpperCase().equals("DATETIME") || orderDetailList.get(orderDetailStr).toUpperCase().equals("DATE")) {
                                vsD += "'" + orderDetail.get(orderDetailStr) + "',";
                            } else {
                                vsD += orderDetail.get(orderDetailStr) + ",";
                            }
                        }
                    }
                    orderItemSqlList = orderItemSqlList.substring(0, orderItemSqlList.length() - 1);
                    orderItemSqlList = orderItemSqlList.replaceAll(",\\s*$", "");
                    vsD = vsD.substring(0, vsD.length() - 1);
                    vsD = vsD.replaceAll(",\\s*$", "");
                    orderItemSqlList += ")" + vsD + ");";
                    orderItemSql.append(orderItemSqlList);
                }


                String id = DataBaseConnection.getInstance().insertOrderIssuedInfo(sysConfig, repeatProcessSql, afterProcessMasterSql, orderItemSql.toString());
                if (!StringUtil.isEmpty(id)) {
                    Map<String, String> resultMap = new HashMap<String, String>();
                    resultMap.put(OpenConstants.ORDER_RETURN_ID, order_id);
                    resultMap.put(OpenConstants.ERP_SN, eas_delivery_number);
                    mapList.add(resultMap);
                }
            }
        } catch (Exception e) {
            log.error("订单信息插入到客户端报错", e);
        }
        return mapList;
    }


    public void notificationOrderIssuedFailure(SysConfig sysConfig, List<Map<String, String>> mapList) {
        PopRequest pr = new PopRequest();
        Map<String, String> headMap = pr.generateHeadMap(ErpTopicName.ErpOrderBackReturn.getMethod(), sysConfig.getKey());
        String returnValue = pr.getPost(sysConfig.getUrlPath(), JSON.toJSONString(mapList), headMap, sysConfig.getSecret());

        if ((returnValue != null) && (!returnValue.equals(""))) {
            Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
            Integer code = apiResponse.getCode();
            if (0 != code) {
                return;
            }
        }
    }
}


