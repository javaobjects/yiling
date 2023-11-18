package com.yiling.dataflow.order.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.check.bo.FlowPurchaseSpecificationIdTotalQuantityBO;
import com.yiling.dataflow.check.dto.request.FlowPurchaseSpecificationIdTotalQuantityRequest;
import com.yiling.dataflow.order.bo.FlowPurchaseInventoryTotalQuantityBO;
import com.yiling.dataflow.order.bo.FlowPurchaseMonthBO;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByPoMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListRequest;
import com.yiling.dataflow.order.dto.request.QueryPurchaseGoodsListRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDetailDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 采购流向表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Repository
public interface FlowPurchaseMapper extends BaseMapper<FlowPurchaseDO> {

    List<String> getByEidAndGoodsInSn(@Param("request") QueryFlowPurchaseByGoodsInSnRequest request);

    Integer deleteFlowPurchaseBydEidAndPoTime(@Param("request") DeleteFlowPurchaseByUnlockRequest request);

    Page<FlowPurchaseDO> page(Page<FlowPurchaseDO> page, @Param("request") QueryFlowPurchaseListPageRequest request);

    List<FlowPurchaseDO> getFlowPurchaseEnterpriseList(@Param("channelId") Integer channelId);

    List<FlowPurchaseDO> getFlowPurchaseSupplierList(@Param("channelId") Integer channelId);

    List<Map<String, Object>> getFlowPurchaseMonthList(@Param("request") QueryFlowPurchaseListRequest request);

    List<Map<String, Object>> getFlowPurchaseAllMonthList(@Param("request") QueryFlowPurchaseListRequest request);

    List<FlowPurchaseDetailDO> getFlowPurchaseDetail(@Param("eid") Long eid, @Param("supplierId")Long supplierId, @Param("time") String time);

    List<Map<String, Object>> getFlowPurchaseGoodsMonthList(@Param("request") QueryPurchaseGoodsListRequest request);

    List<String> getPurchaseGoodsNameList();

    List<FlowPurchaseSpecificationIdTotalQuantityBO> getSpecificationIdTotalQuantityByPoTime(@Param("request") FlowPurchaseSpecificationIdTotalQuantityRequest request);

    /** ======================================== 备份采购流向 ================================================ */

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
     * @param poTimeEnd
     * @return
     */
    Integer initBackupData(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("poTimeEnd") String poTimeEnd);


    /**
     * 根据采购时间备份数据
     *
     * @param tableName
     * @param backupTableName
     * @param poTimeStart
     * @param poTimeEnd
     * @return
     */
    Integer insertBackupDataByPoTime(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("poTimeStart") String poTimeStart, @Param("poTimeEnd") String poTimeEnd);

    /**
     * 根据 备份表名称、采购时间 查询备份数据主键
     *
     * @param poTimeStart
     * @param poTimeEnd
     * @return
     */
    List<Long> getBackupTableIdListByPoTime(@Param("backupTableName") String backupTableName, @Param("poTimeStart") String poTimeStart, @Param("poTimeEnd") String poTimeEnd);

    /**
     * 根据主键删除flow_purchase中数据
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
    Integer deleteByDelFlagAndEid(@Param("eid") Long eid, @Param("poMonth") String poMonth);

    /**
     * 根据采购时间物理删除
     *
     * @param poTimeEnd
     * @return
     */
    Integer deleteByPoTimeEnd(@Param("poTimeEnd") String poTimeEnd);


    List<FlowPurchaseDO> getCantMatchGoodsQuantityList(@Param("eidList") List<Long> eidList, @Param("monthTime") String monthTime);

    Integer getSpecificationIdCount(Long specificationId);

    /**
     * 根据eid, goods_in_sn, yl_goods_id, enterprise_name统计采购库存总数
     *
     * @return
     */
    List<FlowPurchaseInventoryTotalQuantityBO> getTotalQuantityForPurchaseInventory();

    // ********************** 第6个月的采购备份 **********************
    /**
     * 根据当前月份查询是否存在备份数据
     *
     * @param eid 企业id
     * @param backupTableName 备份表名称
     * @param poMonth 月份
     * @return
     */
    Long getOneByEidAndPoMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("poMonth") String poMonth);

    /**
     * 根据月份删除备份表中数据
     *
     * @param eid 企业id
     * @param poMonth 月份
     * @return
     */
    Integer deleteByEidAndPoMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("poMonth") String poMonth);

    /**
     * 根据月份把线上表数据保存到备份表中
     *
     * @param eid 企业id
     * @param backupTableName 备份表名称
     * @param tableName 线上表名称
     * @param poMonth 月份
     * @return
     */
    Integer insertBackupDataByEidAndPoMonth(@Param("eid") Long eid, @Param("backupTableName") String backupTableName, @Param("tableName") String tableName, @Param("poMonth") String poMonth);
    // ********************** 第6个月的采购备份 **********************

    /**
     * 根据企业ID、购进月份查询此月份之前的所有历史数据月份列表
     *
     * @param eid 企业id
     * @param poMonth 购进月份
     * @return
     */
    List<FlowPurchaseMonthBO> getMonthListByEidAndPoMonth(@Param("eid") Long eid, @Param("poMonth") String poMonth);

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
    Page<FlowPurchaseDO> pageBackup(Page<FlowPurchaseDO> page, @Param("request") QueryFlowPurchaseBackupListPageRequest request);

    Integer saveBatchBackup(@Param("backupTableName") String backupTableName, @Param("list") List<FlowPurchaseDO> list);

    Date getMaxPoTimeByEid(@Param("eid") Long eid);

    Integer selectFlowPurchaseCount(@Param("request") UpdateFlowIndexRequest request);

    Page<FlowPurchaseDO> selectFlowPurchasePage(Page<FlowPurchaseDO> page,@Param("request") UpdateFlowIndexRequest request);

    Page<FlowPurchaseDO>  getPageByPoMonth(Page<FlowPurchaseDTO> page, @Param("request") QueryFlowPurchaseByPoMonthPageRequest request);

    List<FlowStatisticsBO> getFlowPurchaseStatistics(@Param("request") QueryFlowStatisticesRequest request);
}
