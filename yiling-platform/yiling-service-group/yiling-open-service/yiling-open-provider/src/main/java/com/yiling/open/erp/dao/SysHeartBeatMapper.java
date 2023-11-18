package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.bo.ErpHeartBeatCountBO;
import com.yiling.open.erp.dto.request.QueryHeartBeatCountRequest;
import com.yiling.open.erp.entity.SysHeartBeatDO;

/**
 * 表sys_heart_beat的数据库操作类
 *
 * @author wanfei.zhang
 * @date 2016-07-21 10:23:06
 */
public interface SysHeartBeatMapper extends BaseMapper<SysHeartBeatDO> {

	int insertSelective(SysHeartBeatDO sysHeartBeat);

	Date getDataBaseCurrentTime();

    /**
     * 根据suid、开始/结束时间 统计心跳数量
     * @param request
     * @return
     */
    List<ErpHeartBeatCountBO> getErpHeartCount(@Param("request") QueryHeartBeatCountRequest request);

    /** ======================================== 备份销售流向 ================================================ */

    /**
     * 删除两个月前的备份表
     *
     * @param backupTableName
     * @return
     */
    Boolean dropLastSecondMonthBackupTable(@Param("backupTableName") String backupTableName);

    /**
     * 创建备份表
     *
     * @param tableName
     * @param backupTableName
     * @return
     */
    Boolean createBackupTable(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName);

    /**
     * 根据创建时间备份心跳数据
     *
     * @param tableName
     * @param backupTableName
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    Integer insertBackupDataByCreateTime(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("createTimeStart") String createTimeStart, @Param("createTimeEnd") String createTimeEnd);

    /**
     * 根据 备份表名称、采购时间 查询是否有备份数据
     *
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    Long getBackupTableCountByCreateTime(@Param("backupTableName") String backupTableName, @Param("createTimeStart") String createTimeStart, @Param("createTimeEnd") String createTimeEnd);

    /**
     * 查询上个月最新创建时间的一条id
     *
     * @return
     */
    List<Map<String, Long>> getIdByMaxCreateTime(@Param("createTimeStart") String createTimeStart, @Param("createTimeEnd") String createTimeEnd);

    /**
     * 根据id删除 sys_heart_beat 中数据
     *
     * @param idList
     * @return
     */
    Integer deleteByIdAndCreateTime(@Param("idList") List<Long> idList, @Param("createTimeEnd") String createTimeEnd);
}
