package com.yiling.open.erp.service;

import java.util.List;
import java.util.Set;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.bo.ErpEntity;

/**
 *
 * @author xiongqi
 * @date 2018/8/20
 */
public interface ErpEntityService {

    List<BaseErpEntity> queryBySuIdAndSuDeptNo(Long suId, String suDeptNo, int pageNo, int pageSize);

    List<BaseErpEntity> queryHistoryList(Long suId, Set<String> suDeptNoSet, Set<String> erpPrimaryKeySet);

    List<? extends ErpEntity> batchInsert(List<? extends ErpEntity> erpEntityList);

    List<BaseErpEntity> save(List<BaseErpEntity> erpEntityList);

    int updateByPrimaryKey(ErpEntity erpEntity);

    int updateSyncInfoByPrimaryKey(ErpEntity erpEntity);

    int deleteByPrimaryKey(ErpEntity erpEntity);

    int deleteByPrimaryKeys(List<Long> primaryKeys);

}
