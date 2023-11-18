package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.open.erp.entity.ErpOrderSendDO;

@Repository
public interface ErpOrderSendMapper extends ErpEntityMapper {
    /**
     * 更改同步状态,通过Id和原有的状态修改状态
     *
     * @return
     */
    Integer updateSyncStatusByStatusAndId(@Param("id") Long id, @Param("syncStatus") Integer syncStatus, @Param("oldSyncStatus") Integer oldSyncStatus, @Param("syncMsg") String syncMsg);

    /**
     * @param id
     * @param syncStatus
     * @param syncMsg
     * @return
     */
    Integer updateSyncStatusAndMsg(@Param("id") Long id, @Param("syncStatus") Integer syncStatus, @Param("syncMsg") String syncMsg);

    /**
     * @param id
     * @param nextSyncTime
     * @param syncMsg
     * @return
     */
    Integer updateMsgAndNextTimeById(@Param("id") Long id, @Param("nextSyncTime") Date nextSyncTime, @Param("syncMsg") String syncMsg);

    /**
     * 通过odId查询发货单
     * @param suId
     * @param odIdList
     * @return
     */
    List<ErpOrderSendDO> findSendOrderByOdId(@Param("suId") Long suId, @Param("suDeptNo") String suDeptNo, @Param("odIdList") List<Long> odIdList,@Param("operType") Integer operType);

    /**
     * 通过ID查询发货单信息
     * @param id
     * @return
     */
    ErpOrderSendDO findById(@Param("id") Long id);

    /**
     * 通过suId查询发货单信息
     * @param suId
     * @return
     */
    List<ErpOrderSendDO> findMd5BySuId(@Param("suId") Long suId);

    /**
     * 发货单xxljob执行方法
     * @return
     */
    List<ErpOrderSendDO> syncOrderSend();

}
