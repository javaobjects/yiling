package com.yiling.erp.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.CacheTaskUtil;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.util.OpenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 库存流向
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@Slf4j
@Service("clientGoodsBatchFlowService")
public class ClientGoodsBatchFlowServiceImpl extends InitErpSysData implements SyncTaskService {

    private static final String GB_TIME = "gb_time";

    @Autowired
    private InitErpConfig initErpConfig;
    @Autowired
    private TaskConfigDao taskConfigDao;

    @Override
    public void syncData() {
        try {
            CacheTaskUtil.getInstance().addCacheData(ErpTopicName.ErpGoodsBatchFlow.getTopicName());
            this.initDataBase(ErpTopicName.ErpGoodsBatchFlow.getTopicName());
            Map<String, String> sqliteData = this.findSqliteData(ErpTopicName.ErpGoodsBatchFlow.getTopicName());
            List<BaseErpEntity> dataList = this.findDataAll(ErpTopicName.ErpGoodsBatchFlow.getMethod());
            log.info("[库存流向信息同步] 本地缓存商品信息条数:" + sqliteData.size() + " | ERP系统库存流向信息条数:" + dataList.size());
            Map<String, BaseErpEntity> map = this.isRepeat(dataList);
            if (map == null) {
                return;
            }
            Map<Integer, Set<String>> handlerMap = this.comparisonData(sqliteData, map);
            List<BaseErpEntity> deleteList = new ArrayList<>();
            List<BaseErpEntity> addList = new ArrayList<>();
            List<BaseErpEntity> updateList = new ArrayList<>();
            Set<String> deleteSet = handlerMap.get(OperTypeEnum.DELETE.getCode());
            Set<String> addSet = handlerMap.get(OperTypeEnum.ADD.getCode());
            Set<String> updateSet = handlerMap.get(OperTypeEnum.UPDATE.getCode());
            log.info(
                    "[库存流向信息同步] 对比以后ERP系统库存删除条数:" + deleteSet.size() + " | 对比以后ERP系统库存增加条数:" + addSet.size() + " | 对比以后ERP系统库存修改条数:" + updateSet.size());
            if (!CollectionUtils.isEmpty(deleteSet)) {
                for (String key : deleteSet) {
                    ErpGoodsBatchFlowDTO erpGoodsBatchFlow = new ErpGoodsBatchFlowDTO();
                    //先判断key是否包含$$$
                    if (key.contains(OpenConstants.SPLITE_SYMBOL)) {
                        String[] keys = key.split(OpenConstants.SPLITE_SYMBOL_FALG);
                        erpGoodsBatchFlow.setGbIdNo(keys[0]);
                        erpGoodsBatchFlow.setSuDeptNo(keys[1]);
                    } else {
                        erpGoodsBatchFlow.setGbIdNo(key);
                        erpGoodsBatchFlow.setSuDeptNo("");
                    }
                    erpGoodsBatchFlow.setOperType(OperTypeEnum.DELETE.getCode());
                    deleteList.add(erpGoodsBatchFlow);
                }
            }

            if (!CollectionUtils.isEmpty(addSet)) {
                for (String key : addSet) {
                    BaseErpEntity baseErpEntity = map.get(key);
                    baseErpEntity.setOperType(OperTypeEnum.ADD.getCode());
                    addList.add(baseErpEntity);
                }
            }

            if (!CollectionUtils.isEmpty(updateSet)) {
                for (String key : updateSet) {
                    BaseErpEntity baseErpEntity = map.get(key);
                    baseErpEntity.setOperType(OperTypeEnum.UPDATE.getCode());
                    updateList.add(baseErpEntity);
                }
            }

            if (!CollectionUtils.isEmpty(deleteList)) {
                this.sendData(deleteList, initErpConfig.getSysConfig(), ErpTopicName.ErpGoodsBatchFlow, OperTypeEnum.DELETE.getCode());
            }
            if (!CollectionUtils.isEmpty(addList)) {
                this.sendData(addList, initErpConfig.getSysConfig(), ErpTopicName.ErpGoodsBatchFlow, OperTypeEnum.ADD.getCode());
            }
            if (!CollectionUtils.isEmpty(updateList)) {
                this.sendData(updateList, initErpConfig.getSysConfig(), ErpTopicName.ErpGoodsBatchFlow, OperTypeEnum.UPDATE.getCode());
            }
        } catch (Exception e) {
            log.error("库存流向信息同步失败", e);
        }finally {
            CacheTaskUtil.getInstance().removeCacheData(ErpTopicName.ErpGoodsBatchFlow.getTopicName());
        }
    }


}
