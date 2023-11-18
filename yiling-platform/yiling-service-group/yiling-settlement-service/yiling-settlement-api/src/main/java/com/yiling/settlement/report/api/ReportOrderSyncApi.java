package com.yiling.settlement.report.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.report.dto.ReportB2bOrderSyncDTO;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.request.QueryFlowOrderPageListRequest;
import com.yiling.settlement.report.dto.request.QueryOrderSyncPageListRequest;
import com.yiling.settlement.report.dto.request.UpdateB2bOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateFlowOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateYlGoodsIdRequest;

/**
 * @author: dexi.yao
 * @date: 2022-05-25
 */
public interface ReportOrderSyncApi {

    /**
     * 查询b2b订单收货信息
     *
     * @param request
     * @return
     */
    Page<ReportB2bOrderSyncDTO> queryB2bOrderSyncInfoPageList(QueryOrderSyncPageListRequest request);

    /**
     * 分页查询流向订单
     *
     * @param request
     * @return
     */
    Page<ReportFlowSaleOrderSyncDTO> queryFlowOrderPageList(QueryFlowOrderPageListRequest request);

    /**
     * 根据流向订单同步表id查询订单收货信息
     *
     * @param id
     * @return
     */
    ReportFlowSaleOrderSyncDTO queryFlowOrderSyncById(Long id);



    /**
     * 根据老的ylGoodsId更新销售流向订单的ylGoodsId
     *
     * @param request
     * @return
     */
    Boolean updateFlowOrderYlGoodsId(UpdateYlGoodsIdRequest request);

    /**
     * 根据b2b订单id查询订单收货信息
     *
     * @param orderId
     * @return
     */
    List<ReportB2bOrderSyncDTO> queryB2bOrderReceiveInfo(Long orderId);

    /**
     * 根据b2b订单同步表id查询订单收货信息
     *
     * @param id
     * @return
     */
    ReportB2bOrderSyncDTO queryB2bOrderSyncById(Long id);

    /**
     * 更新b2b订单的订单标识
     *
     * @param request
     * @return
     */
    Boolean updateB2bOrderIdentification(UpdateB2bOrderIdenRequest request);

    /**
     * 更新流向订单的订单标识
     *
     * @param request
     * @return
     */
    Boolean updateFlowOrderIdentification(UpdateFlowOrderIdenRequest request);

    /**
     * 查询b2b订单同步表的订单数量
     *
     * @param request
     * @return
     */
    Long queryB2bOrderCount(QueryOrderSyncPageListRequest request);

    /**
     * 查询流向订单同步表的订单数量
     *
     * @param request
     * @return
     */
    Long queryFlowOrderCount(QueryFlowOrderPageListRequest request);

    /**
     * 同步流向订单
     */
    void syncFlowOrder();

    /**
     * 初始化b2b订单数据
     *
     * @return
     */
    Boolean initB2bOrderData();

    /**
     * 初始化订单数据
     *
     * @return
     */
    Boolean initMemberOrderData();

    /**
     * 初始化订单数据
     *
     * @return
     */
    Boolean initFlowOrderData();
}
