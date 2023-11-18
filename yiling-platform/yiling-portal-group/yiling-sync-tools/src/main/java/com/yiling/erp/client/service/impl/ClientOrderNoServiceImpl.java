package com.yiling.erp.client.service.impl;

import java.util.List;
import java.util.Map;

import com.yiling.erp.client.util.SqlPaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.dao.PushConfigDao;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.PushConfig;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.erp.client.util.PopRequest;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service("clientOrderNoService")
public class ClientOrderNoServiceImpl implements SyncTaskService {

    @Autowired
    public InitErpConfig erpCommon;

    @Autowired
    public PushConfigDao pushConfigDao;

    @Override
    public void syncData() {
        try {
            //然后判断key是否合法
            SysConfig sysConfig = erpCommon.getSysConfig();
            if (sysConfig == null) {
                return;
            }
            if (StringUtils.isEmpty(sysConfig.getKey())) {
                return;
            }
            if (StringUtils.isEmpty(sysConfig.getSecret())) {
                return;
            }
            if (StringUtils.isEmpty(sysConfig.getUrlPath())) {
                return;
            }

            PushConfig pushConfig = null;
            //查询推送订单数据源配置
            List<PushConfig> list = pushConfigDao.executeQueryPushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from push_config");
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            pushConfig = list.get(0);
            SysConfig sysConfigPush = InitErpConfig.toSysConfig(pushConfig);
            String dataSql = SqlPaginationUtil.getTopSql("select order_id,third_code from yiling_order  where  third_code<>' ' and erp_flag=0" ,sysConfigPush.getDbType(),10);
            List<Map<Object, Object>> mapList = DataBaseConnection.getInstance().executeQuery(sysConfigPush, dataSql);
            if (CollUtil.isNotEmpty(mapList)) {
                for (Map<Object, Object> oneMap : mapList) {
                    PopRequest pr = new PopRequest();
                    Map<String, String> headMap = pr.generateHeadMap(ErpTopicName.ErpOrderErpSn.getMethod(), sysConfig.getKey());
                    String returnValue = pr.getPost(sysConfig.getUrlPath(), JSON.toJSONString(oneMap), headMap, sysConfig.getSecret());
                    if ((returnValue != null) && (!returnValue.equals(""))) {
                        Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
                        Integer code = apiResponse.getCode();
                        if (200 != code) {
                            log.error("回写eas订单编号报错{}",JSON.toJSONString(oneMap));
                            continue;
                        }
                        //成功
                        DataBaseConnection.getInstance().updateErpSn(sysConfigPush, "update yiling_order set erp_flag=1 where order_id=" + oneMap.get("order_id"));
                    }
                }
            }
        } catch (Exception e) {
            log.error("eas订单编号回写失败", e);
        }
    }
}
