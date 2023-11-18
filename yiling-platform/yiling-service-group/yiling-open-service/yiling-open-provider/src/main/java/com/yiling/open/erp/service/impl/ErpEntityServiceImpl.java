package com.yiling.open.erp.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.bo.ErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.service.ErpEntityService;

/**
 *
 * @author xiongqi
 * @date 2018/8/22
 */
public abstract class ErpEntityServiceImpl implements ErpEntityService {

    static final int BATCH_INSERT_SIZE = 1000;
    static final int PAGE_SIZE = 10000;

    public abstract ErpEntityMapper getErpEntityDao();

    @Override
    public List<BaseErpEntity> queryBySuIdAndSuDeptNo(Long suId, String suDeptNo, int pageNo, int pageSize) {
        Objects.requireNonNull(suId);
        if (pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize > PAGE_SIZE) {
            pageSize = PAGE_SIZE;
        }

        List<BaseErpEntity> list = getErpEntityDao().queryBySuIdAndSuDeptNo(suId, suDeptNo, (pageNo - 1) * pageSize, pageSize);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<BaseErpEntity> queryHistoryList(Long suId, Set<String> suDeptNoSet, Set<String> erpPrimaryKeySet) {
        Objects.requireNonNull(suId);
        if (CollectionUtils.isEmpty(suDeptNoSet) || CollectionUtils.isEmpty(erpPrimaryKeySet)) {
            return Collections.emptyList();
        }
        return getErpEntityDao().queryHistoryList(suId, suDeptNoSet, erpPrimaryKeySet);
    }

    @Override
    public List<? extends ErpEntity> batchInsert(List<? extends ErpEntity> erpEntityList) {
        if (CollectionUtils.isEmpty(erpEntityList)) {
            return Collections.emptyList();
        }

        if (erpEntityList.size() <= BATCH_INSERT_SIZE) {
            getErpEntityDao().batchInsert(erpEntityList);
            return erpEntityList;
        }

        for (int i = 0; i < erpEntityList.size(); i += BATCH_INSERT_SIZE) {
            if (i + BATCH_INSERT_SIZE < erpEntityList.size()) {
                getErpEntityDao().batchInsert(erpEntityList.subList(i, i + BATCH_INSERT_SIZE));
            } else {
                getErpEntityDao().batchInsert(erpEntityList.subList(i, erpEntityList.size()));
            }
        }
        return erpEntityList;
    }

    @Override
    public List<BaseErpEntity> save(List<BaseErpEntity> erpEntityList) {
        if (CollectionUtils.isEmpty(erpEntityList)) {
            return Collections.emptyList();
        }
        batchInsert(erpEntityList);
        return erpEntityList;
    }

    @Override
    public int updateByPrimaryKey(ErpEntity erpEntity) {
        Objects.requireNonNull(erpEntity);
        Objects.requireNonNull(erpEntity.getPrimaryKey());
        return getErpEntityDao().updateByPrimaryKey(erpEntity);
    }

    @Override
    public int updateSyncInfoByPrimaryKey(ErpEntity erpEntity) {
        Objects.requireNonNull(erpEntity);
        Objects.requireNonNull(erpEntity.getPrimaryKey());
        return getErpEntityDao().updateSyncInfoByPrimaryKey(erpEntity);
    }

    @Override
    public int deleteByPrimaryKey(ErpEntity erpEntity) {
        Objects.requireNonNull(erpEntity);
        Objects.requireNonNull(erpEntity.getPrimaryKey());
        return getErpEntityDao().deleteByPrimaryKey(erpEntity);
    }

    @Override
    public int deleteByPrimaryKeys(List<Long> primaryKeys) {
        if (CollectionUtils.isEmpty(primaryKeys)) {
            return 0;
        }
        return getErpEntityDao().deleteByPrimaryKeys(primaryKeys);
    }
}
