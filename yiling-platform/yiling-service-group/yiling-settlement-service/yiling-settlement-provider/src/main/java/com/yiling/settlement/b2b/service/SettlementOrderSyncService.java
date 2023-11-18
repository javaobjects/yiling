package com.yiling.settlement.b2b.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.b2b.dto.SettlementOrderSyncDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettOrderDataPageListRequest;
import com.yiling.settlement.b2b.dto.request.UpdateOrderSyncSettStatusRequest;
import com.yiling.settlement.b2b.entity.SettlementOrderSyncDO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-04-07
 */
public interface SettlementOrderSyncService extends BaseService<SettlementOrderSyncDO> {

    /**
     * 根据订单号生成结算单所需要的数据
     *
     * @param orderCode
     * @return
     */
    Boolean createSettOrderSync(String orderCode);

    /**
     * 根据订单号生成结算单所需要的尾款支付超时的订单数据
     *
     * @param orderCode
     * @return
     */
    Boolean createSettOrderCancelSync(String orderCode);

    /**
     * 根据订单同步记录表id重新同步 订单数据
     *
     * @param ids
     * @return
     */
    void syncSettOrderSyncByIds(List<Long> ids);

    /**
     * 根据订单号查询订单数据
     *
     * @param orderCode
     * @return
     */
    SettlementOrderSyncDTO querySettOrderSyncByOrderCode(String orderCode);

    /**
     * 订单账期还款通知结算单业务
     *
     * @param orderCode
     * @return
     */
    Boolean cashRepaymentNotifySett(String orderCode);

    /**
     * 查询结算单订单同步数据列表
     *
     * @param request
     * @return
     */
    Page<SettlementOrderSyncDTO> querySettOrderSyncPageList(QuerySettOrderDataPageListRequest request);

    /**
     * 根据同步状态 查询结算单订单同步数据列表
     *
     * @param request
     * @return
     */
    Page<SettlementOrderSyncDTO> pageListBySyncStatus(QuerySettOrderDataPageListRequest request);

    /**
     * 更新订单同步表的订单结算状态
     *
     * @param request
     * @return
     */
    Boolean updateOrderSyncSettStatus(UpdateOrderSyncSettStatusRequest request);

}
