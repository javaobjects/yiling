package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ErpOrderPurchaseDeliveryDO;

/**
 * <p>
 * 入库单明细表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2022-03-18
 */
@Repository
public interface ErpOrderPurchaseDeliveryMapper extends ErpEntityMapper, BaseMapper<ErpOrderPurchaseDeliveryDO> {

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
     * 通过ID查询发货单信息
     * @param id
     * @return
     */
    ErpOrderPurchaseDeliveryDO findById(@Param("id") Long id);

    /**
     * 通过suId查询发货单信息
     * @param suId
     * @return
     */
    List<ErpOrderPurchaseDeliveryDO> findMd5BySuId(@Param("suId") Integer suId);

    /**
     * xxljob执行方法
     * @return
     */
    List<ErpOrderPurchaseDeliveryDO> syncOrderPurchaseDelivery();

}
