package com.yiling.settlement.report.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.bo.FlowPurchaseInventoryBO;
import com.yiling.settlement.report.bo.RebatedGoodsCountBO;
import com.yiling.settlement.report.dto.ReportDetailB2bDTO;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageByReportIdRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.entity.ReportDetailB2bDO;
import com.yiling.settlement.report.enums.ReportRebateStatusEnum;

/**
 * <p>
 * 返利明细表-b2b订单 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-20
 */
public interface ReportDetailB2bService extends BaseService<ReportDetailB2bDO> {

    /**
     * 根据报表id驳回b2b报表明细
     *
     * @param reportId
     * @param eid
     * @return 需要更新的库存
     */
    Map<String, FlowPurchaseInventoryBO> rejectB2bDetail(Long reportId, Long eid);

    /**
     * 根据报表id和订单id查询b2b订单报表明细
     *
     * @param request
     * @return
     */
    Page<ReportDetailB2bDTO> queryReportDetailB2bPage(QueryReportDetailB2bPageRequest request);

    /**
     * 根据报表id批量查询b2b订单报表明细
     *
     * @param request
     * @return
     */
    Page<ReportDetailB2bDTO> queryReportDetailPageByReportIdList(QueryReportDetailB2bPageByReportIdRequest request);

    /**
     * 查询b2b报表明细的订单数
     *
     * @param request
     * @return
     */
    Long queryB2bRebateOrderCount(QueryReportDetailB2bPageRequest request);

    /**
     * 根据报表id和返利状态查询报表明细
     *
     * @param reportId
     * @param rebateStatusEnum
     * @return
     */
    List<ReportDetailB2bDTO> queryB2bDetailByReportId(Long reportId, ReportRebateStatusEnum rebateStatusEnum);


    /**
     * 根据以岭品id，内码，渠道查询已返利的报表
     *
     * @param request
     * @return
     */
    List<RebatedGoodsCountBO> queryRebateGoods(@Param("request") List<QueryStockOccupyPageRequest> request);
}
