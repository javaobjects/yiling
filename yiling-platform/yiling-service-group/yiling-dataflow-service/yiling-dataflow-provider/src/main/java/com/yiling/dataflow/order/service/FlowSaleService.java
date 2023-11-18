package com.yiling.dataflow.order.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowCrmGoodsBO;
import com.yiling.dataflow.order.bo.FlowSaleCurrentMonthCountBO;
import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.FlowOrderExportReportDetailDTO;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.DeleteFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.FlowSaleMonthCountRequest;
import com.yiling.dataflow.order.dto.request.QueryCrmGoodsRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowOrderExportReportPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleBySoMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistsRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSalePageByEidAndGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.UpdateCrmGoodsCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateEnterpriseCrmCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 流向销售明细信息表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
public interface FlowSaleService extends BaseService<FlowSaleDO> {
    /**
     * 通过op库主键和商业公司查询流向是否存在
     * @param request
     * @return
     */
    Integer deleteFlowSaleBySoIdAndEid(DeleteFlowSaleRequest request);

    /**
     * 通过主键修改数据标记
     * @param idList
     * @param dataTag
     * @return
     */
    Integer updateDataTagByIdList(List<Long> idList, Integer dataTag);

    /**
     * 通过主键id列表删除
     *
     * @param idList
     * @return
     */
    Integer deleteByIdList(List<Long> idList);

    /**
     * 通过op库主键和商业公司查询流向列表数据
     * @param request
     * @return
     */
    List<FlowSaleDO> getFlowSaleDTOBySoIdAndEid(QueryFlowSaleRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Integer updateFlowSaleBySoIdAndEid(SaveOrUpdateFlowSaleRequest request);

    /**
     * 新增流向
     * @param request
     * @return
     */
    FlowSaleDO insertFlowSale(SaveOrUpdateFlowSaleRequest request);

    /**
     * 通过主键修改流向信息
     * @param requestList
     * @return
     */
    Integer updateFlowSaleByIds(List<SaveOrUpdateFlowSaleRequest> requestList);
    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowSaleDO> page(QueryFlowPurchaseListPageRequest request);

    /**
     * 流向数据导出统计
     * @param request
     * @return
     */
    List<FlowOrderExportReportDetailDTO> getOrderFlowReport(QueryFlowOrderExportReportPageRequest request);

    /**
     * 通过eid和so_time删除销售流向详情
     * @param request
     * @return
     */
    Integer deleteFlowSaleBydEidAndSoTime(DeleteFlowSaleByUnlockRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Boolean updateFlowSaleById(SaveOrUpdateFlowSaleRequest request);


    /**
     * 查询列表分页, 标识云仓的商业
     *
     * @return
     */
    Page<FlowSaleDO> flowSaleYunCangPage(QueryFlowPurchaseListPageRequest request);

    /**
     * 根据eid、goodsInSn查询分页
     *
     * @param request
     * @return
     */
    Page<FlowSaleDO> pageByEidAndGoodsInSn(QueryFlowSalePageByEidAndGoodsInSnRequest request);

    /**
     * 销售流向根据企业标签同步返利
     */
    void flowSaleReportSyncByEnterpriseTag();

    /**
     * 根据id更新同步返利状态为已同步
     *
     * @param idList
     * @return
     */
    Boolean updateReportSyncStatusByIdList(List<Long> idList);

    void syncFlowSaleSpec();

    List<FlowSaleDO> getCantMatchGoodsQuantityList(List<Long> eidList, String monthTime);

    void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request);

    Integer getFlowSaleExistsCount(QueryFlowSaleExistsRequest request);

    BigDecimal getFlowSaleExistsQuantity(QueryFlowSaleExistsRequest request);

    boolean isUsedSpecificationId(Long specificationId);

    /**
     * 当前月份销售数据总条数
     *
     * @param request
     * @return
     */
    List<FlowSaleCurrentMonthCountBO> getMonthCount(FlowSaleMonthCountRequest request);

    /**
     * 标记crm
     * @param crmIds
     */
    void updateFlowSaleCrmSign(List<Long> crmIds);
    /**
     * 标记crm客户内码
     * @param eids
     */
    void updateFlowSaleCrmInnerSign(List<Long> eids);

    /**
     * 标记crm商品内码
     * @param eids
     */
    void updateFlowSaleCrmGoodsSign(List<Long> eids);

    /**
     * 查询备份表列表分页
     * @param request
     * @return
     */
    Page<FlowSaleDO> pageBackup(QueryFlowPurchaseBackupListPageRequest request);

    /**
     * 根据id查询详情，可以查询逻辑删除的
     *
     * @param ids
     * @return
     */
    List<FlowSaleDO> getByIdsContainsDeleteFlag(List<Long> ids);

    /**
     * 通过商业公司id和单号查询流向数据
     * @param eid
     * @param soNo
     * @return
     */
    List<FlowSaleDO> getByEidAndSoNo(Long eid, String soNo);

    /**
     * 根据企业id获取最新的销售时间
     *
     * @return
     */
    Date getMaxSoTimeByEid(Long eid);


    /**
     * 批量刷新索引
     */
    int refreshFlowSale(UpdateFlowIndexRequest request);

    /**
     * 批量刷新索引
     */
    int refreshFlowSaleBakup(UpdateFlowIndexRequest request);

    /**
     * 获取未映射crm商品
     * @return
     */
    List<FlowCrmGoodsBO> getUnmappedCrmGoods(QueryCrmGoodsRequest request);

    Integer getCountByEidAndSoTime(Long eid, Date startTime, Date endTime);

    /**
     * 根据企业id、销售时间查询是否有流向数据
     *
     * @param request
     * @return true / false
     */
    boolean isHaveDataByEidAndSoTime(QueryFlowSaleExistRequest request);

    Page<FlowSaleDTO> getPageBySoMonth(QueryFlowSaleBySoMonthPageRequest request);

    void updateCrmGoodsCode(UpdateCrmGoodsCodeRequest request);

    void updateEnterpriseCrmCodeCode(UpdateEnterpriseCrmCodeRequest request);

    List<FlowStatisticsBO> getFlowSaleStatistics(QueryFlowStatisticesRequest request);
}
