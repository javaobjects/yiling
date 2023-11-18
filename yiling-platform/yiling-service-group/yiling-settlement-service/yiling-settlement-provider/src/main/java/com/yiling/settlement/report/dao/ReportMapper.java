package com.yiling.settlement.report.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.report.dto.ReportPurchaseStockOccupyDTO;
import com.yiling.settlement.report.dto.request.QueryReportGoodsRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.entity.ReportDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 报表表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Repository
public interface ReportMapper extends BaseMapper<ReportDO> {

    /**
     * 查询库占用记录
     *
     * @param page
     * @param request
     * @return
     */
    Page<ReportPurchaseStockOccupyDTO> queryStockOccupyPage(Page<ReportPurchaseStockOccupyDTO> page, @Param("request") QueryStockOccupyPageRequest request);

    /**
     * 根据以岭商品id和商品内码判查询未驳回的报表明细条数
     *
     * @param request
     * @return
     */
    Integer queryExitReportByGoodsCount(@Param("request") QueryReportGoodsRequest request);
}
