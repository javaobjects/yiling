package com.yiling.erp.client.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yiling.erp.client.common.Constants;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.CacheTaskUtil;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.util.OpenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Slf4j
@Service("clientCustomerService")
public class ClientCustomerServiceImpl extends InitErpSysData implements SyncTaskService {

    @Autowired
    private InitErpConfig          initErpConfig;

    @Override
    public void syncData() {
        Long startTime=System.currentTimeMillis();
        try {
            CacheTaskUtil.getInstance().addCacheData(ErpTopicName.ErpCustomer.getTopicName());
            this.initDataBase(ErpTopicName.ErpCustomer.getTopicName());
            Map<String, String> sqliteData = this.findSqliteData(ErpTopicName.ErpCustomer.getTopicName());
            List<BaseErpEntity> dataList = this.findDataAll(ErpTopicName.ErpCustomer.getMethod());
            log.info("[客户信息同步] 本地缓存客户信息条数:" + sqliteData.size() + " | ERP系统客户信息条数:" + dataList.size());
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
                "[客户信息同步] 对比以后ERP系统客户删除条数:" + deleteSet.size() + " | 对比以后ERP系统客户增加条数:" + addSet.size() + " | 对比以后ERP系统客户修改条数:" + updateSet.size());
            if (!CollectionUtils.isEmpty(deleteSet)) {
                for (String key : deleteSet) {
                    ErpCustomerDTO erpCustomers = new ErpCustomerDTO();
                    // 先判断key是否包含$$$
                    if (key.contains(OpenConstants.SPLITE_SYMBOL)) {
                        String[] keys = key.split(OpenConstants.SPLITE_SYMBOL_FALG);
                        erpCustomers.setInnerCode(keys[0]);
                        erpCustomers.setSuDeptNo(keys[1]);
                    } else {
                        erpCustomers.setInnerCode(key);
                        erpCustomers.setSuDeptNo("");
                    }
                    erpCustomers.setOperType(OperTypeEnum.DELETE.getCode());
                    deleteList.add(erpCustomers);
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

            // 发送消息
            if (!CollectionUtils.isEmpty(deleteList)) {
                log.debug("发送消息，客户信息同步，删除");
                this.sendData(deleteList, initErpConfig.getSysConfig(), ErpTopicName.ErpCustomer, OperTypeEnum.DELETE.getCode());
            }
            if (!CollectionUtils.isEmpty(addList)) {
                log.debug("发送消息，客户信息同步，新增");
                this.sendData(addList, initErpConfig.getSysConfig(), ErpTopicName.ErpCustomer, OperTypeEnum.ADD.getCode());
            }
            if (!CollectionUtils.isEmpty(updateList)) {
                log.debug("发送消息，客户信息同步，更新");
                this.sendData(updateList, initErpConfig.getSysConfig(), ErpTopicName.ErpCustomer, OperTypeEnum.UPDATE.getCode());
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(Constants.SYNC_EXCEPTION_NAME, System.currentTimeMillis()-startTime,"客户信息", e));
        }finally {
            CacheTaskUtil.getInstance().removeCacheData(ErpTopicName.ErpCustomer.getTopicName());
        }
    }
}
