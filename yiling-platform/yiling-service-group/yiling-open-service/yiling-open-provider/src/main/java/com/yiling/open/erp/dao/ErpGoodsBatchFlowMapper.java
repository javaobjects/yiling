package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ErpGoodsBatchFlowDO;

/**
 * <p>
 * 库存流向明细信息表 Dao 接口
 * </p>
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@Repository
public interface ErpGoodsBatchFlowMapper extends ErpEntityMapper, BaseMapper<ErpGoodsBatchFlowDO> {

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
     * @param id
     * @param syncMsg
     * @return
     */
    Integer retryGoodsBatchFlow(@Param("id") Long id,
                                @Param("syncMsg") String syncMsg);


    /**
     * job调度方法
     * @return
     */
    List<ErpGoodsBatchFlowDO> syncGoodsBatchFlow();


    Integer updateOperTypeGoodsBatchFlowBySuId(@Param("suId") Long suId);

    /**
     * 硬删除流向库存数据
     * @param suId
     * @param createTime
     * @return
     */
    Integer deleteGoodsBatchFlowBySuId(@Param("suId") Long suId,@Param("createTime") Date createTime);

    /** ======================================== 备份采购流向 ================================================ */

    /**
     * 物理删除oper_type = 3、sync_status = 2 的
     *
     * @return
     */
    Integer deleteByOperTypeAndSyncStatusOrGbNumber(@Param("suId") Long suId);

    /**
     * 物理删除oper_type = 3、sync_status = 3、六个月之前的
     *
     * @return
     */
    Integer deleteByGbTimeEndAndOperTypeAndSyncStatus(@Param("gbTimeEnd") String gbTimeEnd, @Param("suId") Long suId);
}
