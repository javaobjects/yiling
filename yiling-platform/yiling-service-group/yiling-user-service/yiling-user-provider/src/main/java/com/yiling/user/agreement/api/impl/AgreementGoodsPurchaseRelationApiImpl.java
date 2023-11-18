package com.yiling.user.agreement.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.agreement.api.AgreementGoodsPurchaseRelationApi;
import com.yiling.user.agreement.dto.request.EditAgreementGoodsPurchaseRelationRequest;
import com.yiling.user.agreement.service.AgreementGoodsPurchaseRelationService;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
@DubboService
public class AgreementGoodsPurchaseRelationApiImpl implements AgreementGoodsPurchaseRelationApi {

    @Autowired
    private AgreementGoodsPurchaseRelationService agreementGoodsPurchaseRelationService;

    @Override
    public Boolean updateBuyerGatherByGid(EditAgreementGoodsPurchaseRelationRequest request) {
        return agreementGoodsPurchaseRelationService.updateBuyerGatherByGid(request);
    }

    @Override
    public Boolean updateBuyerGatherByGid(List<Long> goodsIds,Long opUserId) {
        return null;
    }
}
