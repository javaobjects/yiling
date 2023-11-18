package com.yiling.open.erp.service.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.DataTagEnum;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.service.ErpDataStatService;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.ErpService;
import com.yiling.open.erp.util.OpenConstants;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2019/3/20
 */
@Service
public abstract class ErpServiceImpl implements ErpService {

    @Resource
    protected RedisDistributedLock redisDistributedLock;

    @Autowired
    private ErpDataStatService erpDataStatService;

    public abstract ErpEntityService getErpEntityManager();

    @Override
    public Boolean syncBaseData(Long suId, String taskNo, List<BaseErpEntity> entityList) {
        if (suId == null) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "suId为空");
        }
        if (StringUtils.isBlank(taskNo)) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "taskNo为空");
        }

        if (CollectionUtils.isEmpty(entityList)) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "同步数据为空");
        }

        Set<String> suDeptNoSet = new LinkedHashSet<>(entityList.size());
        Set<String> erpPrimaryKeySet = new LinkedHashSet<>(entityList.size());
        for (BaseErpEntity entity : entityList) {
            entity.setSyncStatus(SyncStatus.SYNCING.getCode());
            suDeptNoSet.add(entity.getSuDeptNo());
            int i = entity.getErpPrimaryKey().indexOf(OpenConstants.SPLITE_SYMBOL);
            if (i == -1) {
                erpPrimaryKeySet.add(entity.getErpPrimaryKey());
            } else {
                erpPrimaryKeySet.add(entity.getErpPrimaryKey().substring(0, entity.getErpPrimaryKey().indexOf(OpenConstants.SPLITE_SYMBOL)));
            }
        }

        ErpEntityService manager = getErpEntityManager();
        // 查询历史数据
        List<BaseErpEntity> historyList = manager.queryHistoryList(suId, suDeptNoSet, erpPrimaryKeySet);
        if (CollectionUtils.isEmpty(historyList)) {
            // 只需要处理 operType=1,2 的数据，全部插入
            List<BaseErpEntity> insertList = new ArrayList<>();
            for (BaseErpEntity entity : entityList) {
                if (Objects.equals(entity.getOperType(), OperTypeEnum.ADD.getCode())
                    || Objects.equals(entity.getOperType(), OperTypeEnum.UPDATE.getCode())) {
                    entity.setOperType(OperTypeEnum.ADD.getCode());
                    //添加逻辑
                    insertList.add(entity);
                }
            }
            if (CollectionUtils.isNotEmpty(insertList)) {
                insertList = manager.save(insertList);
                // 入库成功，handlerData里面不能报错，必须自己处理异常
                handlerData(insertList);
            }
        } else {
            List<BaseErpEntity> insertList = new ArrayList<>();
            List<BaseErpEntity> updateList = new ArrayList<>();
            List<Long> deleteList = new ArrayList<>();
            // 缓存待删除的数据
            Set<BaseErpEntity> removeSet = new HashSet<>();

            for (BaseErpEntity entity : entityList) {
                for (BaseErpEntity historyEntity : historyList) {
                    // 药品状态
                    if (StringUtils.equals(entity.getErpPrimaryKey(), historyEntity.getErpPrimaryKey())) {
                        // 加这个条件是为了避免重复删除
                        if (Objects.equals(entity.getOperType(), OperTypeEnum.DELETE.getCode())
                            && Objects.equals(historyEntity.getOperType(), OperTypeEnum.DELETE.getCode())) {
                            removeSet.add(entity);
                        } else {
                            entity.setPrimaryKey(historyEntity.getPrimaryKey());
                        }
                        break;
                    }
                }
                if (Objects.equals(entity.getOperType(), OperTypeEnum.DELETE.getCode())) {
                    if (entity.getPrimaryKey() == null) {
                        // ERP同步工具判断为删除，但是op库查询不到，这种情况为异常数据，直接删除
                        removeSet.add(entity);
                    } else {
                        deleteList.add(entity.getPrimaryKey());
                    }
                } else if (entity.getPrimaryKey() == null) {
                    entity.setOperType(OperTypeEnum.ADD.getCode());
                    insertList.add(entity);
                } else {
                    entity.setOperType(OperTypeEnum.UPDATE.getCode());
                    updateList.add(entity);
                }
            }

            // 移除已经删除过的数据和被非法删除的数据
            if (CollectionUtils.isNotEmpty(removeSet)) {
                entityList.removeAll(removeSet);
            }

            if (CollectionUtils.isNotEmpty(insertList)) {
                manager.save(insertList);
            }

            if (CollectionUtils.isNotEmpty(updateList)) {
                for (BaseErpEntity entity : updateList) {
                    manager.updateByPrimaryKey(entity);
                }
            }

            if (!ErpTopicName.ErpOrderSend.getMethod().equals(taskNo) && !ErpTopicName.ErpOrderPurchaseDelivery.getMethod().equals(taskNo)) {
                if (CollectionUtils.isNotEmpty(deleteList)) {
                    manager.deleteByPrimaryKeys(deleteList);
                }
            }
            // 入库成功，handlerData里面不能报错，必须自己处理异常
            handlerData(entityList);
        }
        return true;
    }

    // 默认方法，子类实现
//    public void handleDataTag(BaseErpEntity entity, Integer dataTag) {
//    }

    

    private void handlerData(List<BaseErpEntity> entityList) {
        //统计数据的变化次数
        for (BaseErpEntity entity : entityList) {
            erpDataStatService.sendDataStat(entity);
            this.onlineData(entity);
        }
    }

    public String getLockName(Long suId, String taskNo) {
        StringBuilder sb = new StringBuilder();
        sb.append("mph-erp-online-lock-").append(suId).append("-").append(taskNo);
        return sb.toString();
    }

}
