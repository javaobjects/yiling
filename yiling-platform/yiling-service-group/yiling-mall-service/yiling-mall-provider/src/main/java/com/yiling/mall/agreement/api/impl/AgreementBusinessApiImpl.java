package com.yiling.mall.agreement.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.dto.AgreementCalculateResultDTO;
import com.yiling.mall.agreement.dto.CalculateRebateApplyDTO;
import com.yiling.mall.agreement.dto.request.AgreementCalculateRequest;
import com.yiling.mall.agreement.dto.request.CalculateRebateApplyRequest;
import com.yiling.mall.agreement.dto.request.CashMallAgreementRequest;
import com.yiling.mall.agreement.dto.request.SaveRebateApplyRequest;
import com.yiling.mall.agreement.service.AgreementBusinessService;
import com.yiling.mall.agreement.service.AgreementImportTaskService;

/**
 * @author: shuang.zhang
 * @date: 2021/6/17
 */
@DubboService
public class AgreementBusinessApiImpl implements AgreementBusinessApi {

    @Autowired
    AgreementBusinessService agreementBusinessService;
    @Autowired
    AgreementImportTaskService agreementImportTaskService;

    @Override
    public Map<Long, Integer> getGoodsLimitByGids(List<Long> gidList, Long buyerEid) {
        return agreementBusinessService.getGoodsLimitByGids(gidList,buyerEid);
    }

    @Override
    public Map<Long, Integer> getB2bGoodsLimitByGids(List<Long> gidList, Long buyerEid) {
        return agreementBusinessService.getB2bGoodsLimitByGids(gidList,buyerEid);
    }

    @Override
    public Map<Long, Integer> getB2bGoodsLimitBySkuIds(List<Long> skuIdList, Long buyerEid) {
        return agreementBusinessService.getB2bGoodsLimitBySkuIds(skuIdList,buyerEid);
    }

    @Override
    public Boolean getGoodsLimitByGidsAndBuyerEid(List<Long> gidList, Long buyerEid) {
        return agreementBusinessService.getGoodsLimitByGidsAndBuyerEid(gidList,buyerEid);
    }

    @Override
    public List<AgreementCalculateResultDTO> calculateCashAgreement(AgreementCalculateRequest request) {
        return agreementBusinessService.calculateCashAgreement(request);
    }

    @Override
    public Boolean calculateRebateAgreementByAgreementId(CashMallAgreementRequest request) {
        return agreementBusinessService.calculateRebateAgreementByAgreementId(request);
    }

    @Override
    public Boolean cashAgreementByAgreementId(CashMallAgreementRequest request) {
        return agreementBusinessService.cashAgreementByAgreementId(request);
    }

	@Override
	public CalculateRebateApplyDTO calculateRebateApply(CalculateRebateApplyRequest rebateApplyRequest) {
		return agreementBusinessService.calculateRebateApply(rebateApplyRequest);
	}

	@Override
	public Boolean saveRebateApply(SaveRebateApplyRequest request) {
		return agreementBusinessService.saveRebateApply(request);
	}

    @Override
    public Boolean calculateRebateAgreementByDay() {
        return agreementBusinessService.calculateRebateAgreementByDay();
    }

    @Override
    public void generateAgree(String taskCode) {
        agreementImportTaskService.generateAgree(taskCode);
    }

}
