package com.yiling.mall.agreement.service;

import java.util.List;
import java.util.Map;

import com.yiling.mall.agreement.dto.AgreementCalculateResultDTO;
import com.yiling.mall.agreement.dto.CalculateRebateApplyDTO;
import com.yiling.mall.agreement.dto.request.AgreementCalculateRequest;
import com.yiling.mall.agreement.dto.request.CalculateRebateApplyRequest;
import com.yiling.mall.agreement.dto.request.CashMallAgreementRequest;
import com.yiling.mall.agreement.dto.request.SaveRebateApplyRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/6/17
 */
public interface AgreementBusinessService {
    /**
     * 批量查询商品采购权限
     *
     * @param gidList  商品ID列表
     * @param buyerEid 买家企业ID
     * @return
     */
    Map<Long, Integer> getGoodsLimitByGids(List<Long> gidList, Long buyerEid);

    /**
     * 批量查询商品采购权限
     *
     * @param gidList  商品ID列表
     * @param buyerEid 买家企业ID
     * @return  value=GoodsLimitStatusEnum
     */
    Map<Long, Integer> getB2bGoodsLimitByGids(List<Long> gidList, Long buyerEid);

    /**
     * 批量查询商品采购权限根据sku
     *
     * @param skuIdList  商品sku ID列表
     * @param buyerEid 买家企业ID
     * @return  value=GoodsLimitStatusEnum
     */
    Map<Long, Integer> getB2bGoodsLimitBySkuIds(List<Long> skuIdList, Long buyerEid);


    /**
     * 批量查询商品采购权限通过商品ID集合，购买EID，配送EId
     *
     * @param gidList  商品ID列表
     * @param buyerEid 买家企业ID
     * @return
     */
    Boolean getGoodsLimitByGidsAndBuyerEid(List<Long> gidList, Long buyerEid);

    /**
     * 订单下单计算现择接口
     * @param request
     * @return  返回协议对应商品返利金额
     */
    List<AgreementCalculateResultDTO> calculateCashAgreement(AgreementCalculateRequest request);

    /**
     * 计算每一天的返利协议Id
     * @return  返回协议对应商品返利金额
     */
    Boolean calculateRebateAgreementByDay();

    /**
     * 计算其中某一个协议
     * @param request
     * @return
     */
    Boolean calculateRebateAgreementByAgreementId(CashMallAgreementRequest request);

    /**
     * 兑付其中某一个协议
     * @param request
     * @return
     */
    Boolean cashAgreementByAgreementId(CashMallAgreementRequest request);

	/**
	 * 根据时间段计算返利明细
	 *
	 * @param rebateApplyRequest
	 * @return
	 */
	CalculateRebateApplyDTO calculateRebateApply(CalculateRebateApplyRequest rebateApplyRequest);

	/**
	 * 返利申请保存
	 *
	 * @param request
	 * @return
	 */
	Boolean saveRebateApply(SaveRebateApplyRequest request);

}
