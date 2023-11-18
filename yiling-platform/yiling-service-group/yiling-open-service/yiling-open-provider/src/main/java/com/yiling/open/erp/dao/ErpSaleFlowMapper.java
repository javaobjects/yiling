package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.bo.ErpSaleFlowMonthBO;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.dto.request.QuerySaleFlowConditionRequest;
import com.yiling.open.erp.entity.ErpSaleFlowDO;

/**
 * <p>
 * 流向销售明细信息表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
@Repository
public interface ErpSaleFlowMapper extends ErpEntityMapper, BaseMapper<ErpSaleFlowDO> {


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
     * @param startSoTime 开始时间
     * @param endSoTime 结束时间
     * @param suId 公司编码
     * @return
     */
    List<ErpSaleFlowDO> findListBySuIdAndSoTime(@Param("startSoTime") Date startSoTime, @Param("endSoTime") Date endSoTime,@Param("suDeptNo") String suDeptNo, @Param("suId") Long suId);

    /**
     * 删除op库数据
     * @param id
     * @return
     */
    Integer deleteFlowById(@Param("id") Long id);

    List<ErpSaleFlowDO> syncSaleFlowPage(@Param("requestList")List<QuerySaleFlowConditionRequest> requestList);

    /**
     * 解封同步OP库数据到线上
     *
     * @return
     */
    List<ErpSaleFlowDO> unLockSynSaleFlow(@Param("request") ErpFlowSealedUnLockRequest request);

    /** ======================================== 备份销售流向 ================================================ */

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
    Integer initBackupData(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("soTimeEnd") String soTimeEnd);


    /**
     * 根据采购时间备份数据
     *
     * @param tableName
     * @param backupTableName
     * @param soTimeEnd
     * @param suId
     * @return
     */
    Integer insertBackupDataBySoTime(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("soTimeEnd") String soTimeEnd, @Param("suId") Long suId);

    /**
     * 根据 备份表名称、采购时间 查询备份数据主键
     *
     * @param soTimeStart
     * @param soTimeEnd
     * @return
     */
    List<Long> getBackupTableIdListBySoTime(@Param("backupTableName") String backupTableName, @Param("soTimeStart") String soTimeStart, @Param("soTimeEnd") String soTimeEnd);

    /**
     * 根据主键删除erp_sale_flow中数据
     *
     * @param soTimeStart
     * @param soTimeEnd
     * @return
     */
    Integer deleteBySoTime(@Param("soTimeStart") String soTimeStart, @Param("soTimeEnd") String soTimeEnd);

    /**
     * 物理删除oper_type = 3、sync_status = 2 的
     *
     * @return
     */
    Integer deleteByOperTypeAndSyncStatus(@Param("suId") Long suId);

    /**
     * 根据销售时间物理删除
     *
     * @param soTimeEnd
     * @param suId
     * @return
     */
    Integer deleteBySoTimeEnd(@Param("soTimeEnd") String soTimeEnd, @Param("suId") Long suId);

    /**
     * 根据企业ID、销售月份查询此月份之前的所有历史数据月份列表
     *
     * @param suId 企业id
     * @param soTime 销售月份
     * @return
     */
    List<ErpSaleFlowMonthBO> getMonthListBySuidAndSoTime(@Param("suId") Long suId, @Param("soTime") String soTime);

    /**
     * 根据采购时间备份数据
     *
     * @param tableName
     * @param backupTableName
     * @param soTimeEnd
     * @param suId
     * @return
     */
    Integer insertBackupDataBySuidAndSoTime(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("soTimeStart") String soTimeStart, @Param("soTimeEnd") String soTimeEnd, @Param("suId") Long suId);

    /**
     * 根据销售时间物理删除
     *
     * @param soTimeStart
     * @param soTimeEnd
     * @param suId
     * @return
     */
    Integer deleteBySuIdAndSoTime(@Param("soTimeStart") String soTimeStart, @Param("soTimeEnd") String soTimeEnd, @Param("suId") Long suId);
}
