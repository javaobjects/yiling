package com.yiling.dataflow.order.dao;

import java.util.List;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowGoodsBatchDetailMonthBO;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDetailDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailByGbMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.statistics.bo.GoodsSpecQuantityBO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * ERP库存汇总同步数据 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-14
 */
@Repository
public interface FlowGoodsBatchDetailMapper extends BaseMapper<FlowGoodsBatchDetailDO> {

    Page<FlowGoodsBatchDetailDO> page(Page<FlowGoodsBatchDetailDO> page, @Param("request") QueryFlowGoodsBatchDetailListPageRequest request);

    Integer deleteFlowGoodsBatchDetailByEidAndDate(@Param("request") DeleteFlowGoodsBatchDetailRequest request);

    /** ======================================== 备份ERP库存汇总 ================================================ */

    /**
     * 创建备份表
     *
     * @param tableName
     * @param backupTableName
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    Boolean createBackupTable(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName);

    /**
     * 添加字段
     *
     * @param backupTableName
     * @return
     */
    Boolean addColumn(@Param("backupTableName") String backupTableName);

    /**
     * 初始化备份表
     *
     * @param tableName
     * @param backupTableName
     * @param gbDetailTimeEnd
     * @return
     */
    Integer initBackupData(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("gbDetailTimeEnd") String gbDetailTimeEnd);


    /**
     * 根据采购时间备份数据
     *
     * @param tableName
     * @param backupTableName
     * @param gbDetailTimeStart
     * @param gbDetailTimeEnd
     * @return
     */
    Integer insertBackupDataBySoTime(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("gbDetailTimeStart") String gbDetailTimeStart, @Param("gbDetailTimeEnd") String gbDetailTimeEnd);

    /**
     * 根据 备份表名称、采购时间 查询备份数据主键
     *
     * @param gbDetailTimeStart
     * @param gbDetailTimeEnd
     * @return
     */
    List<Long> getBackupTableIdListByGbDetailTime(@Param("backupTableName") String backupTableName, @Param("gbDetailTimeStart") String gbDetailTimeStart, @Param("gbDetailTimeEnd") String gbDetailTimeEnd);

    /**
     * 根据主键删除erp_sale_flow中数据
     *
     * @param gbDetailTimeStart
     * @param gbDetailTimeEnd
     * @return
     */
    Integer deleteByGbDetailTime(@Param("gbDetailTimeStart") String gbDetailTimeStart, @Param("gbDetailTimeEnd") String gbDetailTimeEnd);

    /**
     * 物理删除oper_type = 3、sync_status = 2 的
     *
     * @return
     */
    Integer deleteByDelFlagAndEid(@Param("eid") Long eid);

    /**
     * 根据销售时间物理删除
     *
     * @param gbDetailTimeEnd
     * @return
     */
    Integer deleteByGbDetailTimeEnd(@Param("gbDetailTimeEnd") String gbDetailTimeEnd);

    List<GoodsSpecQuantityBO> findGoodsDateQuantityByEidAndDateTime(@Param("eid") Long eid, @Param("dateTime") String dateTime, @Param("specificationIdList") List<Long> specificationIdList);

    List<FlowGoodsBatchDetailDO> getEndMonthCantMatchGoodsQuantityList(@Param("eidList") List<Long> eidList, @Param("dateTime") String dateTime);

    Integer getSpecificationIdCount(Long specificationId);

    // ********************** 第6个月的库存备份 **********************
    /**
     * 根据当前库存月份查询是否存在数据
     *
     * @param eid 企业id
     * @param backupTableName 表名称
     * @param gbDetailMonth 当前库存月份
     * @return
     */
    Long getOneByEidAndGbDetailMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("gbDetailMonth") String gbDetailMonth);

    /**
     * 根据当前库存月份删除备份表中数据
     *
     * @param eid 企业id
     * @param gbDetailMonth 当前库存月份
     * @return
     */
    Integer deleteByEidAndGbDetailMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("gbDetailMonth") String gbDetailMonth);

    /**
     * 根据当前库存月份把线上表数据保存到备份表中
     *
     * @param eid 企业id
     * @param backupTableName 备份表名称
     * @param tableName 线上表名称
     * @param gbDetailMonth 当前库存月份
     * @return
     */
    Integer insertBackupDataByEidAndGbDetailMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("tableName") String tableName, @Param("gbDetailMonth") String gbDetailMonth);
    // ********************** 第6个月的库存备份 **********************

    // ********************** 第7个月及之前的库存备份 **********************

    /**
     * 根据企业ID、当前库存月份查询此月份之前的所有历史数据月份列表
     *
     * @param eid 企业id
     * @param gbDetailMonth 当前库存月份结束时间
     * @return
     */
    List<FlowGoodsBatchDetailMonthBO> getMonthListByEidAndGbDetailMonth(@Param("eid") Long eid, @Param("gbDetailMonth") String gbDetailMonth);

    /**
     * 查询备份表列表分页
     *
     * @param page
     * @param request
     * @return
     */
    Page<FlowGoodsBatchDetailDO> pageBackup(Page<FlowGoodsBatchDetailDO> page, @Param("request") QueryFlowGoodsBatchDetailBackupListPageRequest request);

    /**
     * 根据id列表删除备份
     *
     * @param idList
     * @return
     */
    Integer deleteById(@Param("backupTableName") String backupTableName, @Param("idList") List<Long> idList);

    Integer saveBatchBackup(@Param("backupTableName") String backupTableName, @Param("list") List<FlowGoodsBatchDetailDO> list);

    Integer selectFlowGoodsBatchDetailCount(@Param("request") UpdateFlowIndexRequest request);

    Page<FlowGoodsBatchDetailDO> selectFlowGoodsBatchDetailPage(Page<FlowGoodsBatchDetailDO> page,@Param("request") UpdateFlowIndexRequest request);

    Page<FlowGoodsBatchDetailDO> getPageByGbMonth(Page<FlowGoodsBatchDetailDTO> page, @Param("request") QueryFlowGoodsBatchDetailByGbMonthPageRequest request);

    List<FlowStatisticsBO> getFlowGoodsBatchStatistics(@Param("request") QueryFlowStatisticesRequest request);
}
