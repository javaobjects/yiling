package com.yiling.dataflow.order.api;


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
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;

/**
 * erp销售流向接口
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
public interface FlowSaleApi {

    /**
     * 通过主键和eid获取采购单删除流向
     * @param request
     * @return
     */
    Integer deleteFlowSaleBySoIdAndEid(DeleteFlowSaleRequest request);

    /**
     * 更新数据标签
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
     * 通过主键和eid获取采购单详情
     * @param request
     * @return
     */
    List<FlowSaleDTO> getFlowSaleDTOBySoIdAndEid(QueryFlowSaleRequest request);

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
    FlowSaleDTO insertFlowSale(SaveOrUpdateFlowSaleRequest request);

    Integer updateFlowSaleByIds(List<SaveOrUpdateFlowSaleRequest> requestList);

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowSaleDTO> page(QueryFlowPurchaseListPageRequest request);

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
    Page<FlowSaleDTO> flowSaleYunCangPage(QueryFlowPurchaseListPageRequest request);

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

    /**
     * 同步商品规格id
     */
    void syncFlowSaleSpec();

    /**
     * 当前月份销售数据总条数
     *
     * @param request
     * @return
     */
    List<FlowSaleCurrentMonthCountBO> getMonthCount(FlowSaleMonthCountRequest request);

    /**
     * 标记crm客户
     * @param eids
     */
    void updateFlowSaleCrmInnerSign(List<Long> eids);

    /**
     * 标记crm标记
     * @param crmIds
     */
    void updateFlowSaleCrmSign(List<Long> crmIds);

    /**
     * 标记crm商品
     * @param eids
     */
    void updateFlowSaleCrmGoodsSign(List<Long> eids);

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
     * 获取未映射crm商品
     * @return
     */
    List<FlowCrmGoodsBO> getUnmappedCrmGoods(QueryCrmGoodsRequest request);

    /**
     * 根据企业id、销售时间查询是否有流向数据
     *
     * @param request
     * @return true / false
     */
    boolean isHaveDataByEidAndSoTime(QueryFlowSaleExistRequest request);

    List<FlowStatisticsBO> getFlowSaleStatistics(QueryFlowStatisticesRequest request);
}
