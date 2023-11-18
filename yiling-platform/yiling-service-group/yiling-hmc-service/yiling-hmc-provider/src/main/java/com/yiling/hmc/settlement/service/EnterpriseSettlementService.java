package com.yiling.hmc.settlement.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementBO;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageBO;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageResultBO;
import com.yiling.hmc.settlement.bo.SettlementEnterprisePageBO;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementRequest;
import com.yiling.hmc.settlement.dto.request.SettlementEnterprisePageRequest;
import com.yiling.hmc.settlement.entity.EnterpriseSettlementDO;

/**
 * <p>
 * 商家结账表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
public interface EnterpriseSettlementService extends BaseService<EnterpriseSettlementDO> {

    /**
     * 根据订单明细id查询终端结算信息
     *
     * @param detailId 订单明细id
     * @return 对应终端结算信息
     */
    EnterpriseSettlementDO queryByDetailId(Long detailId);

    /**
     * 根据订单id查询终端结算信息
     *
     * @param orderId 订单id
     * @return 对应终端结算信息
     */
    List<EnterpriseSettlementDO> listByOrderId(Long orderId);

    /**
     * 以岭给商家药品对账
     *
     * @param request 查询条件
     * @return 对账信息
     */
    List<SettlementEnterprisePageBO> pageSettlement(SettlementEnterprisePageRequest request);

    /**
     * 统计未结算完成的订单数量
     *
     * @param request 查询条件
     * @return 未结算完成的订单数量
     */
    List<SettlementEnterprisePageBO> countUnSettlementOrder(SettlementEnterprisePageRequest request);

    /**
     * 统计已结算完成的订单数量
     *
     * @param request 查询条件
     * @return 已结算完成的订单数量
     */
    List<SettlementEnterprisePageBO> countEnSettlementOrder(SettlementEnterprisePageRequest request);

    /**
     * 以岭与商家结算，明细表分页查询
     *
     * @param request 查询条件
     * @return 结算明细
     */
    Page<EnterpriseSettlementPageBO> pageList(EnterpriseSettlementPageRequest request);

    /**
     * 以岭与商家结算明细表分页,总数量和金额查询
     *
     * @param request 查询条件
     * @return 结算信息
     */
    EnterpriseSettlementPageResultBO pageResult(EnterpriseSettlementPageRequest request);

    /**
     * 以岭与商家的结算
     *
     * @param requestList 导入的数据
     * @param opUserId 操作人
     * @param opTime 操作时间
     * @return 成功和失败的返回
     */
    List<EnterpriseSettlementBO> settlement(List<EnterpriseSettlementRequest> requestList, Long opUserId, Date opTime);

    /**
     * 以岭与商家的结算导入
     *
     * @param request 结算导入信息
     * @return 成功/失败
     */
    boolean importSettlementData(EnterpriseSettlementRequest request);
}