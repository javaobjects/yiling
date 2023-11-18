package com.yiling.settlement.report.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.dto.RebatedGoodsCountDTO;
import com.yiling.settlement.report.dto.ReportDTO;
import com.yiling.settlement.report.dto.ReportPurchaseStockOccupyDTO;
import com.yiling.settlement.report.dto.request.AdjustReportRequest;
import com.yiling.settlement.report.dto.request.ConfirmReportRequest;
import com.yiling.settlement.report.dto.request.CreateReportB2bRequest;
import com.yiling.settlement.report.dto.request.CreateReportFlowRequest;
import com.yiling.settlement.report.dto.request.QueryReportGoodsRequest;
import com.yiling.settlement.report.dto.request.QueryReportPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.dto.request.RebateByReportRequest;
import com.yiling.settlement.report.dto.request.RejectReportRequest;
import com.yiling.settlement.report.entity.ReportDO;

/**
 * <p>
 * 报表表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
public interface ReportService extends BaseService<ReportDO> {

    /**
     * 生成b2b订单返利报表
     *
     * @param request
     * @return
     */
    Long createB2bReport(CreateReportB2bRequest request);

    /**
     * 生成流向订单返利报表
     *
     * @param request
     * @return
     */
    Long createFlowReport(CreateReportFlowRequest request);

    /**
     * 查询报表列表
     *
     * @param request
     * @return
     */
    Page<ReportDTO> queryReportPage(QueryReportPageRequest request);

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
     * 根据商品查询未驳回的报表明细
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
}
