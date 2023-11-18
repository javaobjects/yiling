package com.yiling.settlement.b2b.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.b2b.bo.B2BSettlementMetadataBO;
import com.yiling.settlement.b2b.dto.SettlementAmountInfoDTO;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementPageListRequest;
import com.yiling.settlement.b2b.dto.request.SubmitSalePaymentRequest;
import com.yiling.settlement.b2b.dto.request.UpdatePaymentStatusRequest;
import com.yiling.settlement.b2b.entity.SettlementDO;
import com.yiling.settlement.b2b.entity.SettlementOrderSyncDO;

/**
 * <p>
 * b2b商家结算单表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-19
 */
public interface SettlementService extends BaseService<SettlementDO> {

    /**
     * 根据当日0点前以签收的b2b订单并生成结算单
     *
     * @param sellerEid 卖家id
     */
    void generateSettlement(Long sellerEid);

    /**
     * 根据各卖家下的订单生成结算单
     *
     * @param request key=卖家id，value=订单集合
     * @return
     */
    B2BSettlementMetadataBO generateSettlementByOrder(Map<Long, List<SettlementOrderSyncDO>> request);

    /**
     * 查询平台结算金额信息
     *
     * @param eid 商家eid，为null时统计全平台
     * @return
     */
    SettlementAmountInfoDTO querySettlementAmountInfo(Long eid);

    /**
     * 分页查询结算单列表
     *
     * @param request
     * @return
     */
    Page<SettlementDTO> querySettlementPageList(QuerySettlementPageListRequest request);

    /**
     * 根据企业付款No更新结算单状态
     *
     * @param list
     * @return
     */
    Boolean updatePaymentStatus(List<UpdatePaymentStatusRequest> list);

    /**
     * 根据企业付款no查询结算订单
     *
     * @param payNo
     * @return
     */
    List<SettlementDTO> querySettlementListByPayNo(List<String> payNo);

    /**
     * 根批量更新结算单
     *
     * @param settlementDOList
     * @return
     */
    Boolean updateSettlementById(List<SettlementDO> settlementDOList);

    /**
     * 根据促销结算单id更新结算单状态为已发起结算并发起打款
     *
     * @param request
     * @return
     */
    Boolean submitSalePayment(SubmitSalePaymentRequest request);

}
