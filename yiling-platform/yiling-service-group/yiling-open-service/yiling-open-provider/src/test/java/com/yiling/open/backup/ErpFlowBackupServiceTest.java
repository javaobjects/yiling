package com.yiling.open.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.BaseTest;
import com.yiling.open.backup.dto.request.FlowPurchaseDeleteBackRequest;
import com.yiling.open.backup.dto.request.FlowSaleDeleteBackRequest;
import com.yiling.open.backup.service.ErpFlowBackupService;
import com.yiling.open.erp.dao.SysHeartBeatMapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * @author: houjie.sun
 * @date: 2022/7/14
 */
public class ErpFlowBackupServiceTest extends BaseTest {

    @Autowired
    private ErpFlowBackupService erpFlowBackupService;

    @Autowired
    private SysHeartBeatMapper sysHeartBeatMapper;


    @Test
    public void erpFlowBackupTest(){
        erpFlowBackupService.erpFlowBackup();
    }

    @Test
    public void cleanErpFlowOssFileTest(){
        erpFlowBackupService.cleanErpFlowOssFile();
    }

    @Test
    public void sysHeartBeatTest(){
        List<Map<String, Long>> lastMonthMaxIdMapList = sysHeartBeatMapper.getIdByMaxCreateTime("2022-06-01 00:00:00", "2022-08-01 23:59:59");
        if (CollUtil.isEmpty(lastMonthMaxIdMapList)) {
            return;
        }
        Map<Long, Long> lastMonthMaxIdMap = new HashMap<>();
        for (Map<String, Long> map : lastMonthMaxIdMapList) {
            lastMonthMaxIdMap.put(map.get("suId").longValue(), map.get("id"));
        }
        // 删除心跳上个月及之前所有数据，仅保留上个月最新一条
        List<Long> idList = new ArrayList<>(lastMonthMaxIdMap.values());
        sysHeartBeatMapper.deleteByIdAndCreateTime(idList, "2021-12-31 23:59:59");
    }

}
