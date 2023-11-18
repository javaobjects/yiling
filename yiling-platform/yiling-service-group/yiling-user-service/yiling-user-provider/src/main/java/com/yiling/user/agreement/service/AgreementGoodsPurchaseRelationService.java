package com.yiling.user.agreement.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.request.EditAgreementGoodsPurchaseRelationRequest;
import com.yiling.user.agreement.entity.AgreementGoodsPurchaseRelationDO;

/**
 * <p>
 * 商品统计表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-09
 */
public interface AgreementGoodsPurchaseRelationService extends BaseService<AgreementGoodsPurchaseRelationDO> {

    /**
     * 通过商品ID修改可以购买的供应商ID集合
     *
     * @param request
     * @return
     */
    Boolean updateBuyerGatherByGid(EditAgreementGoodsPurchaseRelationRequest request);

    /**
     * 通过商品ID统计当前可购买的渠道商id集合
     *
     * @param goodsIds 商品集合
     * @param opUserId 操作人
     * @param buyerEid 购买者ID
     * @return
     */
    Boolean updateBuyerGatherByGid(List<Long> goodsIds, Long buyerEid, Long opUserId);

    /**
     * 通过商品ID删除可购买的渠道商id集合
     *
     * @param goodsIds 商品集合
     * @param opUserId 操作人
     * @param buyerEid 购买者ID
     * @return
     */
    Boolean deleteBuyerGatherByGid(List<Long> goodsIds, Long buyerEid, Long opUserId);
}
