package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.bo.ErpPurchaseFlowMonthBO;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.entity.ErpPurchaseFlowDO;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
@Repository
public interface ErpPurchaseFlowMapper extends ErpEntityMapper, BaseMapper<ErpPurchaseFlowDO> {

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
     * 获取某一天的流向
     * @param startPoTime 开始时间
     * @param endPoTime 结束时间
     * @param suId 公司编码
     * @return
     */
    List<ErpPurchaseFlowDO> findListBySuIdAndPoTime(@Param("startPoTime") Date startPoTime, @Param("endPoTime") Date endPoTime,@Param("suDeptNo") String suDeptNo, @Param("suId") Long suId);

    /**
     * 删除op库数据
     * @param id
     * @return
     */
    Integer deleteFlowById(@Param("id") Long id);

    List<ErpPurchaseFlowDO> syncPurchaseFlow();

    /**
     * 解封同步OP库数据到线上
     *
     * @return
     */
    List<ErpPurchaseFlowDO> unLockSynPurchaseFlow(@Param("request") ErpFlowSealedUnLockRequest request);

    /** ======================================== 备份采购流向 ================================================ */

    /**
     * 创建备份表
     *
     * @param tableName
     * @param backupTableName
     * @return
     */
    Boolean createBackupTable(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName);

    /**
     * 初始化备份表
     *
     * @param tableName
     * @param backupTableName
     * @return
     */
    Integer initBackupData(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("poTimeEnd") String poTimeEnd);


    /**
     * 根据采购时间备份数据
     *
     * @param tableName
     * @param backupTableName
     * @param poTimeEnd
     * @param suId
     * @return
     */
    Integer insertBackupDataByPoTime(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("poTimeEnd") String poTimeEnd, @Param("suId") Long suId);

    /**
     * 根据 备份表名称、采购时间 查询备份数据主键
     *
     * @param poTimeStart
     * @param poTimeEnd
     * @return
     */
    List<Long> getBackupTableIdListByPoTime(@Param("backupTableName") String backupTableName, @Param("poTimeStart") String poTimeStart, @Param("poTimeEnd") String poTimeEnd);

    /**
     * 根据主键删除erp_purchase_flow中数据
     *
     * @param poTimeStart
     * @param poTimeEnd
     * @return
     */
    Integer deleteByPoTime(@Param("poTimeStart") String poTimeStart, @Param("poTimeEnd") String poTimeEnd);

    /**
     * 物理删除oper_type = 3、sync_status = 2 的
     *
     * @return
     */
    Integer deleteByOperTypeAndSyncStatus(@Param("suId") Long suId);

    /**
     * 根据采购时间物理删除
     *
     * @param poTimeEnd
     * @param suId
     * @return
     */
    Integer deleteByPoTimeEnd(@Param("poTimeEnd") String poTimeEnd, @Param("suId") Long suId);

    /**
     * 根据企业ID、销售月份查询此月份之前的所有历史数据月份列表
     *
     * @param suId 企业id
     * @param poTime 销售月份
     * @return
     */
    List<ErpPurchaseFlowMonthBO> getMonthListBySuidAndPoTime(@Param("suId") Long suId, @Param("poTime") String poTime);

    /**
     * 根据采购时间备份数据
     *
     * @param tableName
     * @param backupTableName
     * @param poTimeEnd
     * @param suId
     * @return
     */
    Integer insertBackupDataByPoTimeStartEnd(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("poTimeStart") String poTimeStart, @Param("poTimeEnd") String poTimeEnd, @Param("suId") Long suId);

    /**
     * 根据采购时间物理删除
     *
     * @param poTimeEnd
     * @param suId
     * @return
     */
    Integer deleteByPoTimeStartEnd(@Param("poTimeStart") String poTimeStart, @Param("poTimeEnd") String poTimeEnd, @Param("suId") Long suId);
}
