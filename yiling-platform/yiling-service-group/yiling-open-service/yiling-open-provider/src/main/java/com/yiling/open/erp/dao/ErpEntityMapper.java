package com.yiling.open.erp.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.bo.ErpEntity;

/**
 *
 * @author xiongqi
 * @date 2018/8/22
 */
public interface ErpEntityMapper {

    List<BaseErpEntity> queryBySuIdAndSuDeptNo(@Param("suId") Long suId, @Param("suDeptNo") String suDeptNo, @Param("start") int start, @Param("count") int count);

    List<BaseErpEntity> queryHistoryList(@Param("suId") Long suId, @Param("suDeptNoSet") Set<String> suDeptNoSet, @Param("erpPrimaryKeySet") Set<String> erpPrimaryKeySet);

    int batchInsert(List<? extends ErpEntity> erpEntityList);

    int updateByPrimaryKey(ErpEntity erpEntity);

    int updateSyncInfoByPrimaryKey(ErpEntity erpEntity);

    int deleteByPrimaryKey(ErpEntity erpEntity);

    int deleteByPrimaryKeys(List<Long> primaryKeys);
}
