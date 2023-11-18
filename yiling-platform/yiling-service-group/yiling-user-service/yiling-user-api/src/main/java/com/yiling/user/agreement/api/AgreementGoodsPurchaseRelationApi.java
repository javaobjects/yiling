package com.yiling.user.agreement.api;


import java.util.List;

import com.yiling.user.agreement.dto.request.EditAgreementGoodsPurchaseRelationRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
public interface AgreementGoodsPurchaseRelationApi {

    /**
     * 通过商品ID修改可以购买的供应商ID集合
     * @param request
     * @return
     */
    Boolean updateBuyerGatherByGid(EditAgreementGoodsPurchaseRelationRequest request);

    /**
     * 通过商品ID统计当前可购买的渠道商id集合
     * @param goodsIds
     * @param opUserId
     * @return
     */
    Boolean updateBuyerGatherByGid(List<Long> goodsIds,Long opUserId);
}
