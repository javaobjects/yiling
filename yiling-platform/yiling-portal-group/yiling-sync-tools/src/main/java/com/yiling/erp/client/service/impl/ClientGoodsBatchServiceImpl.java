package com.yiling.erp.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.CacheTaskUtil;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.util.OpenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service("clientGoodsBatchService")
public class ClientGoodsBatchServiceImpl extends InitErpSysData implements SyncTaskService {

    @Autowired
    private InitErpConfig initErpConfig;

    @Override
    public void syncData() {
        try {
            CacheTaskUtil.getInstance().addCacheData(ErpTopicName.ErpGoodsBatch.getTopicName());
            this.initDataBase(ErpTopicName.ErpGoodsBatch.getTopicName());
            Map<String, String> sqliteData = this.findSqliteData(ErpTopicName.ErpGoodsBatch.getTopicName());
            List<BaseErpEntity> dataList = this.findDataAll(ErpTopicName.ErpGoodsBatch.getMethod());
            log.info("[库存信息同步] 本地缓存商品信息条数:" + sqliteData.size() + " | ERP系统库存信息条数:" + dataList.size());
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
                    "[库存信息同步] 对比以后ERP系统库存删除条数:" + deleteSet.size() + " | 对比以后ERP系统库存增加条数:" + addSet.size() + " | 对比以后ERP系统库存修改条数:" + updateSet.size());
            if (!CollectionUtils.isEmpty(deleteSet)) {
                for (String key : deleteSet) {
                    ErpGoodsBatchDTO erpGoodsBatch = new ErpGoodsBatchDTO();
                    //先判断key是否包含$$$
                    if (key.contains(OpenConstants.SPLITE_SYMBOL)) {
                        String[] keys = key.split(OpenConstants.SPLITE_SYMBOL_FALG);
                        erpGoodsBatch.setGbIdNo(keys[0]);
                        erpGoodsBatch.setSuDeptNo(keys[1]);
                    } else {
                        erpGoodsBatch.setGbIdNo(key);
                        erpGoodsBatch.setSuDeptNo("");
                    }
                    erpGoodsBatch.setOperType(OperTypeEnum.DELETE.getCode());
                    deleteList.add(erpGoodsBatch);
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
                this.sendData(deleteList, initErpConfig.getSysConfig(), ErpTopicName.ErpGoodsBatch, OperTypeEnum.DELETE.getCode());
            }
            if (!CollectionUtils.isEmpty(addList)) {
                this.sendData(addList, initErpConfig.getSysConfig(), ErpTopicName.ErpGoodsBatch, OperTypeEnum.ADD.getCode());
            }
            if (!CollectionUtils.isEmpty(updateList)) {
                this.sendData(updateList, initErpConfig.getSysConfig(), ErpTopicName.ErpGoodsBatch, OperTypeEnum.UPDATE.getCode());
            }
        } catch (Exception e) {
            log.error("出库信息同步失败", e);
        }finally {
            CacheTaskUtil.getInstance().removeCacheData(ErpTopicName.ErpGoodsBatch.getTopicName());
        }
    }

}
