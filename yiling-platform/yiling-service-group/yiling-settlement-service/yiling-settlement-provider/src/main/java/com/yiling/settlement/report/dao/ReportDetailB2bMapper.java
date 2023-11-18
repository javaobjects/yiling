package com.yiling.settlement.report.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.settlement.report.bo.RebatedGoodsCountBO;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.entity.ReportDetailB2bDO;

/**
 * <p>
 * 返利明细表-b2b订单 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-20
 */
@Repository
public interface ReportDetailB2bMapper extends BaseMapper<ReportDetailB2bDO> {


    /**
     * 查询报表的订单明细-订单id分组
     *
     * @param page
     * @param request
     * @return
     */
    Page<Long> queryReportB2bOrderPageGroupByOrder(Page<ReportDetailB2bDO> page, @Param("request") QueryReportDetailB2bPageRequest request);

    /**
     * 根据以岭品id，内码，渠道查询已返利的报表
     *
     * @param request
     * @return
     */
    List<RebatedGoodsCountBO> queryRebateGoods(@Param("request") List<QueryStockOccupyPageRequest> request);
}
