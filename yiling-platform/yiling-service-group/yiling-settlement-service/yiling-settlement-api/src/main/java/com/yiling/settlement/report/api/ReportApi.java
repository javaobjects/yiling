package com.yiling.settlement.report.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.report.dto.RebatedGoodsCountDTO;
import com.yiling.settlement.report.dto.ReportDTO;
import com.yiling.settlement.report.dto.ReportDetailB2bDTO;
import com.yiling.settlement.report.dto.ReportDetailFlowDTO;
import com.yiling.settlement.report.dto.ReportLogDTO;
import com.yiling.settlement.report.dto.ReportPurchaseStockOccupyDTO;
import com.yiling.settlement.report.dto.request.AdjustReportRequest;
import com.yiling.settlement.report.dto.request.ConfirmReportRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageByReportIdRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailFlowPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportGoodsRequest;
import com.yiling.settlement.report.dto.request.QueryReportPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.dto.request.RebateByReportRequest;
import com.yiling.settlement.report.dto.request.RejectReportRequest;

/**
 * @author: dexi.yao
 * @date: 2022-05-20
 */
public interface ReportApi {

    /**
     * 查询报表列表
     *
     * @param request
     * @return
     */
    Page<ReportDTO> queryReportPage(QueryReportPageRequest request);

    /**
     * 根据id查询报表
     *
     * @param id
     * @return
     */
    ReportDTO queryReportById(Long id);

    /**
     * 查询受否提示会员存在退款
     *
     * @param reportId
     * @param eid
     * @return
     */
    Boolean queryMemberRefundIsAlert(Long reportId, Long eid);

    /**
     * 根据报表id驳回报表
     *
     * @param request
     * @return
     */
    Boolean rejectReport(RejectReportRequest request);

    /**
     * 根据报表id确认报表
     *
     * @param request
     * @return
     */
    Boolean confirm(ConfirmReportRequest request);

    /**
     * 根据报表id调整金额
     *
     * @param request
     * @return
     */
    Boolean adjust(AdjustReportRequest request);

    /**
     * 根据报表id查询日志
     *
     * @param reportId
     * @return
     */
    List<ReportLogDTO> queryLogList(Long reportId);

    /**
     * 查询b2b订单报表明细
     *
     * @param request
     * @return
     */
    Page<ReportDetailB2bDTO> queryReportDetailB2bPage(QueryReportDetailB2bPageRequest request);

    /**
     * 根据明细id查询报表明细
     *
     * @param id
     * @return
     */
    ReportDetailB2bDTO queryB2bReportDetailById(Long id);

    /**
     * 根据明细id查询报表明细
     *
     * @param id
     * @return
     */
    ReportDetailFlowDTO queryFlowReportDetailById(Long id);

    /**
     * 根据报表id批量查询b2b订单报表明细
     *
     * @param request
     * @return
     */
    Page<ReportDetailB2bDTO> queryReportDetailB2bPageByReportId(QueryReportDetailB2bPageByReportIdRequest request);

    /**
     * 查询流向订单报表明细
     *
     * @param request
     * @return
     */
    Page<ReportDetailFlowDTO> queryReportDetailFlowPage(QueryReportDetailFlowPageRequest request);

    /**
     * 查询b2b报表明细的订单数
     *
     * @param request
     * @return
     */
    Long queryB2bRebateOrderCount(QueryReportDetailB2bPageRequest request);

    /**
     * 查询流向报表明细的订单数
     *
     * @param request
     * @return
     */
    Long queryFlowRebateOrderCount(QueryReportDetailFlowPageRequest request);

    /**
     * 报表勾选返利
     *
     * @param request
     * @return
     */
    Boolean rebateByReport(RebateByReportRequest request);

    /**
     * 查询商品的已返利数量
     *
     * @param request
     * @return
     */
    List<RebatedGoodsCountDTO> queryRebateCount(List<QueryStockOccupyPageRequest> request);

    /**
     * 查询库占用记录
     *
     * @param request
     * @return
     */
    Page<ReportPurchaseStockOccupyDTO> queryStockOccupyPage(QueryStockOccupyPageRequest request);

    /**
     * 根据以岭商品id和商品内码判断是否可以修改以岭品绑定关系
     *
     * @param request
     * @return TRUE可修改，反之
     */
    Boolean isExitReportByGoods(QueryReportGoodsRequest request);

    /**
     * 根据eid查询是否存在正在生成的报表任务
     *
     * @param eid
     * @return TRUE存在，反之
     */
    Boolean isInProductionReportTask(Long eid);

}
