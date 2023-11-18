package com.yiling.settlement.report.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.request.QueryFlowOrderPageListRequest;
import com.yiling.settlement.report.entity.FlowSaleOrderSyncDO;

/**
 * <p>
 * 流向销售明细信息同步表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-25
 */
@Repository
public interface FlowSaleOrderSyncMapper extends BaseMapper<FlowSaleOrderSyncDO> {

    /**
     * 查询流向订单主键
     *
     * @param page
     * @param request
     * @return
     */
    Page<ReportFlowSaleOrderSyncDTO> queryFlowOrderPageList(Page<ReportFlowSaleOrderSyncDTO> page, @Param("request") QueryFlowOrderPageListRequest request);

    /**
     * 根据flowId查询流向订单
     *
     * @param page
     * @param flowIdList
     * @return
     */
    Page<ReportFlowSaleOrderSyncDTO> queryFlowOrderList(Page<ReportFlowSaleOrderSyncDTO> page, @Param("flowIdList") List<Long> flowIdList);

    /**
     * 根据流向表id查询是否存在
     *
     * @param flowIdList
     * @return
     */
    List<Long> querySyncByFlowIdList(@Param("flowIdList") List<Long> flowIdList);

    /**
     * 查询流向订单数量
     *
     * @param request
     * @return
     */
    Long queryOrderCount(@Param("request") QueryFlowOrderPageListRequest request);

}
