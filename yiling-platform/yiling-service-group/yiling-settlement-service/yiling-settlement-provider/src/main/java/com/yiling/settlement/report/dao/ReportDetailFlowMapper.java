package com.yiling.settlement.report.dao;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.report.bo.RebatedGoodsCountBO;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailFlowPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.entity.ReportDetailB2bDO;
import com.yiling.settlement.report.entity.ReportDetailFlowDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-20
 */
@Repository
public interface ReportDetailFlowMapper extends BaseMapper<ReportDetailFlowDO> {

    /**
     * 查询报表的订单明细-订单编号分组
     *
     * @param page
     * @param request
     * @return
     */
    Page<String> queryReportFlowOrderPageGroupByOrder(Page<ReportDetailFlowDO> page, @Param("request") QueryReportDetailFlowPageRequest request);

    /**
     * 根据以岭品id，内码，渠道查询已返利的报表
     *
     * @param request
     * @return
     */
    List<RebatedGoodsCountBO> queryRebateGoods(@Param("request") List<QueryStockOccupyPageRequest> request);
}
