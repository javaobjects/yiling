package com.yiling.open.erp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ErpSettlementDO;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-05
 */
@Repository
public interface ErpSettlementMapper extends ErpEntityMapper,BaseMapper<ErpSettlementDO> {
    /**
     * 更改同步状态,通过Id和原有的状态修改状态
     *
     * @return
     */
    Integer updateSyncStatusByStatusAndId(@Param("id") Long id, @Param("syncStatus") Integer syncStatus,
                                          @Param("oldSyncStatus") Integer oldSyncStatus, @Param("syncMsg") String syncMsg);

    /**
     * @param id
     * @param syncStatus
     * @param syncMsg
     * @return
     */
    Integer updateSyncStatusAndMsg(@Param("id") Long id, @Param("syncStatus") Integer syncStatus,
                                   @Param("syncMsg") String syncMsg);


    /**
     * 重新结算信息
     * @return
     */
    List<ErpSettlementDO> syncSettlement();
}
