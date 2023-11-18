package com.yiling.settlement.report.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.request.QueryFlowOrderPageListRequest;
import com.yiling.settlement.report.dto.request.UpdateFlowOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateYlGoodsIdRequest;
import com.yiling.settlement.report.entity.FlowSaleOrderSyncDO;
import com.yiling.settlement.report.enums.ReportStatusEnum;

/**
 * <p>
 * 流向销售明细信息同步表 服务类
 * </p>
 *
 * @author dexi.yao`
 * @date 2022-05-25
 */
public interface FlowSaleOrderSyncService extends BaseService<FlowSaleOrderSyncDO> {

    /**
     * 更新以岭品id
     *
     * @param request
     * @return
     */
    Boolean updateYlGoodsId(UpdateYlGoodsIdRequest request);

    /**
     * 根据id查询订单列表
     *
     * @param idList
     * @return
     */
    List<ReportFlowSaleOrderSyncDTO> queryOrderListByIdList(List<Long> idList);

    /**
     * 分页查询流向订单
     *
     * @param request
     * @return
     */
    Page<ReportFlowSaleOrderSyncDTO> queryFlowOrderPageList(QueryFlowOrderPageListRequest request);

    /**
     * 同步流向订单
     * 如果订单已经生成返利则忽略更新
     *
     * @param flowIdList
     * @return
     */
    Boolean syncFlowOrder(List<Long> flowIdList);

    /**
     * 根据报表id驳回同步的订单
     *
     * @param reportId
     * @param statusEnum
     */
    void rejectOrderSync(Long reportId, ReportStatusEnum statusEnum);

    /**
     * 更新流向订单的订单标识
     *
     * @param request
     * @return
     */
    Boolean updateFlowOrderIdentification(UpdateFlowOrderIdenRequest request);

    //    /**
    //     * 根据流向订单信息同步表主键更新订单标识为库存不足（只更新订单状态为正常的）
    //     *
    //     * @param idList
    //     * @return
    //     */
    //    Boolean updateFlowOrderIdentById(List<Long> idList);

    /**
     * 查询流向订单数量
     *
     * @param request
     * @return
     */
    Long queryOrderCount(QueryFlowOrderPageListRequest request);

    /**
     * 初始化订单数据
     *
     * @return
     */
    Boolean initFlowOrderData();
}
