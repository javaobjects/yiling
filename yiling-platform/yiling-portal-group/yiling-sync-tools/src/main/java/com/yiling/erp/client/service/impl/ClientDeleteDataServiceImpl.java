package com.yiling.erp.client.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.ReadProperties;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.PushConfig;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.erp.client.util.PopRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/8/9
 */
@Slf4j
@Service("clientDeleteDataService")
public class ClientDeleteDataServiceImpl implements SyncTaskService {


    @Autowired
    private PushConfigDao pushConfigDao;

    @Autowired
    private InitErpConfig initErpConfig;

    @Override
    public void syncData() {
        try {
            log.debug("[删除任务同步] 任务开始");
            int orderSize = Integer.valueOf(ReadProperties.getProperties("orderSize")).intValue();
            PushConfig pushConfig = null;
            //查询推送订单数据源配置
            List<PushConfig> list = pushConfigDao.executeQueryPushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from push_config");
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            pushConfig = list.get(0);
            SysConfig sysConfig = InitErpConfig.toSysConfig(pushConfig);
            //获取需要推送的订单接口
            PopRequest pr = new PopRequest();
            Map<String, String> headParams = pr.generateHeadMap(ErpTopicName.ErpDeleteData.getMethod(), initErpConfig.getSysConfig().getKey());
            String returnValue = pr.getPost(initErpConfig.getSysConfig().getUrlPath(), "{taskNo:" + ErpTopicName.ErpOrderBill.getMethod() + "}", headParams, initErpConfig.getSysConfig().getSecret());
            //插入到erp系统里
            log.debug("[删除任务同步] 服务端返回信息"+returnValue);
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
            log.error("删除任务同步到服务器报错", e);
        }
    }

    private List<Map<String, String>> insertOrder(SysConfig sysConfig, String returnObject) {
        JSONArray jsonArray = JSONArray.parseArray(returnObject);
        if (jsonArray == null || jsonArray.isEmpty()) {
            return null;
        }
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if (StringUtils.isEmpty(object.getString("dataId"))) {
                    return null;
                }
                String data = object.getString("dataId");
                DataBaseConnection.getInstance().updateErpSn(sysConfig, "DELETE t,tt from yiling_bill t, yiling_order_bill tt WHERE t.eas_delivery_number ='" + data + "' and tt.eas_delivery_number in (" + data + ")");
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put(OpenConstants.ORDER_ID, data);
                resultMap.put(OpenConstants.ERP_SN, ErpTopicName.ErpOrderBill.getMethod());
                mapList.add(resultMap);
            }
        } catch (Exception e) {
//            PopRequest popRequest = new PopRequest();
//            ClientToolLogDTO erpLog = new ClientToolLogDTO();
//            erpLog.setClientLog(e.getMessage() + "堆栈信息：" + ExceptionUtils.getFullStackTrace(e));
//            erpLog.setMothedNo(ErpTopicName.ErpDeleteData.getMethod());
//            erpLog.setLogType(Integer.valueOf(3));
//            popRequest.addErpLog(erpLog, initErpConfig.getSysConfig());
            log.error("删除任务插入到客户端报错", e);
            e.printStackTrace();
        }
        return mapList;
    }


    public void notificationOrderIssuedFailure(SysConfig sysConfig, List<Map<String, String>> mapList) {
        PopRequest pr = new PopRequest();
        Map<String, String> headMap = pr.generateHeadMap(ErpTopicName.ErpDeleteDataReturn.getMethod(), sysConfig.getKey());
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
