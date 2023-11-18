package com.yiling.erp.client.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.erp.client.util.CacheTaskUtil;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpOrderSendDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.util.OpenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service("clientOderSendService")
public class ClientOrderSendServiceImpl extends InitErpSysData implements SyncTaskService {

    @Autowired
    private InitErpConfig initErpConfig;

    @Autowired
    private TaskConfigDao taskConfigDao;

    @Override
    public void syncData() {
        try {
            CacheTaskUtil.getInstance().addCacheData(ErpTopicName.ErpOrderSend.getTopicName());
            this.initDataBase(ErpTopicName.ErpOrderSend.getTopicName());
            Map<String, String> sqliteData = this.findSqliteData(ErpTopicName.ErpOrderSend.getTopicName());
            List<BaseErpEntity> dataList = this.findDataAll(ErpTopicName.ErpOrderSend.getMethod());
            log.info("[发货单同步] 本地缓存发货单信息条数:" + sqliteData.size() + " | ERP系统发货单信息条数:" + dataList.size());
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
            log.info("[发货单同步] 对比以后ERP系统发货单删除条数:" + deleteSet.size() + " | 对比以后ERP系统发货单增加条数:" + addSet.size() + " | 对比以后ERP系统发货单修改条数:" + updateSet.size());
            if (!CollectionUtils.isEmpty(deleteSet)) {
                for (String key : deleteSet) {
                    ErpOrderSendDTO erpOrderSend = new ErpOrderSendDTO();
                    //先判断key是否包含$$$
                    if (key.contains(OpenConstants.SPLITE_SYMBOL)) {
                        String[] keys = key.split(OpenConstants.SPLITE_SYMBOL_FALG);
                        erpOrderSend.setOsiId(keys[0]);
                        erpOrderSend.setSuDeptNo(keys[1]);
                    } else {
                        erpOrderSend.setOsiId(key);
                        erpOrderSend.setSuDeptNo("");
                    }
                    erpOrderSend.setOperType(OperTypeEnum.DELETE.getCode());
                    deleteList.add(erpOrderSend);
                }
                taskConfigDao.deleteSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, ErpTopicName.ErpOrderSend.getTopicName(), deleteList);
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

            //直接删除修改和删除的缓存信息
            if (!CollectionUtils.isEmpty(deleteList)) {
                taskConfigDao.deleteSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, ErpTopicName.ErpOrderSend.getTopicName(), deleteList);
            }

            List<BaseErpEntity> allList = new ArrayList<>();
            allList.addAll(addList);
            allList.addAll(updateList);
            if (!CollectionUtils.isEmpty(allList)) {
                allList = allList.stream().sorted(Comparator.comparing(BaseErpEntity::getErpPrimaryKey)).collect(Collectors.toList());
                this.sendData(allList, initErpConfig.getSysConfig(), ErpTopicName.ErpOrderSend, OperTypeEnum.ALL.getCode());
            }

        } catch (Exception e) {
            log.error("发货单同步报错", e);
        }finally {
            CacheTaskUtil.getInstance().removeCacheData(ErpTopicName.ErpOrderSend.getTopicName());
        }
    }
}
