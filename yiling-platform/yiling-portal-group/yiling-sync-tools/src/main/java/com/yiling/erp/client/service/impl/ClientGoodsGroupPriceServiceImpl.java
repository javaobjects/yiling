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
import com.yiling.open.erp.dto.ErpGoodsGroupPriceDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.util.OpenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service("clientGoodsGroupPriceService")
public class ClientGoodsGroupPriceServiceImpl extends InitErpSysData implements SyncTaskService {

    @Autowired
    private InitErpConfig initErpConfig;

    @Override
    public void syncData() {
        try {
            CacheTaskUtil.getInstance().addCacheData(ErpTopicName.ErpGroupPrice.getTopicName());
            this.initDataBase(ErpTopicName.ErpGroupPrice.getTopicName());
            Map<String, String> sqliteData = this.findSqliteData(ErpTopicName.ErpGroupPrice.getTopicName());
            List<BaseErpEntity> dataList = this.findDataAll(ErpTopicName.ErpGroupPrice.getMethod());
            log.info("[客户分组定价信息同步] 本地缓存客户分组定价信息条数:" + sqliteData.size() + " | ERP系统客户分组定价信息条数:" + dataList.size());
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
            log.info("[客户分组定价信息同步] 对比以后ERP系统客户分组定价删除条数:" + deleteSet.size() + " | 对比以后ERP系统客户分组定价增加条数:" + addSet.size() + " | 对比以后ERP系统客户分组定价修改条数:" + updateSet.size());
            if (!CollectionUtils.isEmpty(deleteSet)) {
                for (String key : deleteSet) {
                    ErpGoodsGroupPriceDTO erpGoodsGroupPriceDTO=new ErpGoodsGroupPriceDTO();
                    //先判断key是否包含$$$
                    if (key.contains(OpenConstants.SPLITE_SYMBOL)) {
                        String[] keys = key.split(OpenConstants.SPLITE_SYMBOL_FALG);
                        erpGoodsGroupPriceDTO.setGgpIdNo(keys[0]);
                        erpGoodsGroupPriceDTO.setSuDeptNo(keys[1]);
                    } else {
                        erpGoodsGroupPriceDTO.setGgpIdNo(key);
                        erpGoodsGroupPriceDTO.setSuDeptNo("");
                    }
                    erpGoodsGroupPriceDTO.setOperType(OperTypeEnum.DELETE.getCode());
                    deleteList.add(erpGoodsGroupPriceDTO);
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
                List<BaseErpEntity> failList1 = this.sendData(deleteList, initErpConfig.getSysConfig(), ErpTopicName.ErpGroupPrice, OperTypeEnum.DELETE.getCode());
            }
            if (!CollectionUtils.isEmpty(addList)) {
                List<BaseErpEntity> failList2 = this.sendData(addList, initErpConfig.getSysConfig(), ErpTopicName.ErpGroupPrice, OperTypeEnum.ADD.getCode());
            }
            if (!CollectionUtils.isEmpty(updateList)) {
                List<BaseErpEntity> failList3 = this.sendData(updateList, initErpConfig.getSysConfig(), ErpTopicName.ErpGroupPrice, OperTypeEnum.UPDATE.getCode());
            }
        } catch (Exception e) {
            log.error("分组定价信息同步失败", e);
        }finally {
            CacheTaskUtil.getInstance().removeCacheData(ErpTopicName.ErpGroupPrice.getTopicName());
        }
    }

}
