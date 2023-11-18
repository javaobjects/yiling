package com.yiling.dataflow.order.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowCrmGoodsBO;
import com.yiling.dataflow.order.bo.FlowSaleCurrentMonthCountBO;
import com.yiling.dataflow.order.bo.FlowSaleMonthBO;
import com.yiling.dataflow.order.dto.FlowOrderExportReportDetailDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.FlowSaleMonthCountRequest;
import com.yiling.dataflow.order.dto.request.QueryCrmGoodsRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowOrderExportReportPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleBySoMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistsRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向销售明细信息表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Repository
public interface FlowSaleMapper extends BaseMapper<FlowSaleDO> {

    /**
     * 流向数据导出统计
     * @param request
     * @return
     */
    List<FlowOrderExportReportDetailDTO> getOrderFlowReport(@Param("request") QueryFlowOrderExportReportPageRequest request);

    Integer deleteFlowSaleBydEidAndPoTime(@Param("request") DeleteFlowSaleByUnlockRequest request);

    Page<FlowSaleDO> page(Page<FlowSaleDO> page, @Param("request") QueryFlowPurchaseListPageRequest request);

    /** ======================================== 备份销售流向 ================================================ */

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
     * @param soTimeEnd
     * @return
     */
    Integer initBackupData(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("soTimeEnd") String soTimeEnd);


    /**
     * 根据采购时间备份数据
     *
     * @param tableName
     * @param backupTableName
     * @param soTimeStart
     * @param soTimeEnd
     * @return
     */
    Integer insertBackupDataBySoTime(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("soTimeStart") String soTimeStart, @Param("soTimeEnd") String soTimeEnd);

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
     * 物理删除 del_flag = 1 的
     *
     * @return
     */
    Integer deleteByDelFlagAndEid(@Param("eid") Long eid, @Param("soMonth") String soMonth);

    /**
     * 根据销售时间物理删除
     *
     * @param soTimeEnd
     * @return
     */
    Integer deleteBySoTimeEnd(@Param("soTimeEnd") String soTimeEnd);

    List<FlowSaleDO> getCantMatchGoodsQuantityList(@Param("eidList") List<Long> eidList, @Param("monthTime") String monthTime);

    Integer getFlowSaleExistsCount(@Param("request") QueryFlowSaleExistsRequest request);

    BigDecimal getFlowSaleExistsQuantity(@Param("request") QueryFlowSaleExistsRequest request);

    Integer getSpecificationIdCount(Long specificationId);

    List<FlowSaleCurrentMonthCountBO> getMonthCount(@Param("request") FlowSaleMonthCountRequest request);


    // ********************** 第6个月的销售备份 **********************
    /**
     * 根据当前月份查询是否存在备份数据
     *
     * @param eid 企业id
     * @param backupTableName 备份表名称
     * @param soMonth 月份
     * @return
     */
    Long getOneByEidAndSoMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("soMonth") String soMonth);

    /**
     * 根据月份删除备份表中数据
     *
     * @param eid 企业id
     * @param soMonth 月份
     * @return
     */
    Integer deleteByEidAndSoMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("soMonth") String soMonth);

    /**
     * 根据月份把线上表数据保存到备份表中
     *
     * @param eid 企业id
     * @param backupTableName 备份表名称
     * @param tableName 线上表名称
     * @param soMonth 月份
     * @return
     */
    Integer insertBackupDataByEidAndSoMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("tableName") String tableName, @Param("soMonth") String soMonth);
    // ********************** 第6个月的销售备份 **********************

    /**
     * 根据企业ID、销售月份查询此月份之前的所有历史数据月份列表
     *
     * @param eid 企业id
     * @param soMonth 销售月份
     * @return
     */
    List<FlowSaleMonthBO> getMonthListByEidAndSoMonth(@Param("eid") Long eid, @Param("soMonth") String soMonth);

    /**
     * 根据id列表删除备份
     *
     * @param idList
     * @return
     */
    Integer deleteById(@Param("backupTableName") String backupTableName, @Param("idList") List<Long> idList);

    /**
     * 查询备份表列表分页
     * @param request
     * @return
     */
    Page<FlowSaleDO> pageBackup(Page<FlowSaleDO> page, @Param("request") QueryFlowPurchaseBackupListPageRequest request);

    Integer saveBatchBackup(@Param("backupTableName") String backupTableName, @Param("list") List<FlowSaleDO> list);

    /**
     * 根据id查询详情，可以查询逻辑删除的
     *
     * @param ids
     * @return
     */
    List<FlowSaleDO> getByIdsContainsDeleteFlag(@Param("ids") List<Long> ids);

    Date getMaxSoTimeByEid(@Param("eid") Long eid);

    Integer selectFlowSaleCount(@Param("request") UpdateFlowIndexRequest request);

    Page<FlowSaleDO> selectFlowSalePage(Page<FlowSaleDO> page,@Param("request") UpdateFlowIndexRequest request);

    List<FlowCrmGoodsBO> getUnmappedCrmGoods(@Param("request") QueryCrmGoodsRequest request);

    Page<FlowSaleDO> getPageBySoMonth(Page<FlowSaleDO> page, @Param("request") QueryFlowSaleBySoMonthPageRequest request);

    List<FlowStatisticsBO> getFlowSaleStatistics(@Param("request") QueryFlowStatisticesRequest request);
}
