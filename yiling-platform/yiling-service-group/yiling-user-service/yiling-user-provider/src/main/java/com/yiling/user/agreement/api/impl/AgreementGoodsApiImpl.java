package com.yiling.user.agreement.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.bo.AgreementGoodsPurchaseRelationBO;
import com.yiling.user.agreement.bo.SupplementaryAgreementGoodsBO;
import com.yiling.user.agreement.bo.TempAgreementGoodsBO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.AgreementGoodsDetailsPageRequest;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsPageRequest;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreement.dto.request.SalePurchaseGoodsRequest;
import com.yiling.user.agreement.service.AgreementGoodsService;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@DubboService
public class AgreementGoodsApiImpl implements AgreementGoodsApi {

    @Autowired
    AgreementGoodsService agreementGoodsService;

    @Override
    public Page<AgreementGoodsDTO> agreementGoodsListPage(QueryAgreementGoodsPageRequest request) {
        return agreementGoodsService.agreementGoodsListPage(request);
    }

    /**
     * 根据AgreementId查询分页商品
     *
     * @param request
     * @return
     */
    @Override
    public Page<AgreementGoodsDTO> getAgreementGoodsByAgreementIdPage(AgreementGoodsDetailsPageRequest request) {
        return agreementGoodsService.getAgreementGoodsByAgreementIdPage(request);
    }

    /**
     * 根据AgreementId查询所有协议商品
     *
     * @param agreementId
     * @return
     */
    @Override
    public List<AgreementGoodsDTO> getAgreementGoodsAgreementIdByList(Long agreementId) {
        return agreementGoodsService.getAgreementGoodsAgreementIdByList(agreementId);
    }

    @Override
    public Map<Long, List<AgreementGoodsDTO>> getAgreementGoodsListByAgreementIds(List<Long> agreementIds) {
        return agreementGoodsService.getAgreementGoodsListByAgreementIds(agreementIds);
    }

    /**
     * 根据商品集合重新查询可购买的配货商
     *
     * @param goodsIds
     * @return
     */
    @Override
    public List<AgreementGoodsPurchaseRelationBO> getBuyerGatherByGoodsIds(List<Long> goodsIds) {
        return agreementGoodsService.getBuyerGatherByGoodsIds(goodsIds);
    }

    @Override
    public Page<AgreementGoodsDTO> getYearPurchaseGoodsPageList(AgreementPurchaseGoodsPageRequest request) {

        return agreementGoodsService.getYearPurchaseGoodsPageList(request);
    }

    @Override
    public Page<SupplementaryAgreementGoodsBO> getSupplementarySaleGoodPageList(AgreementPurchaseGoodsPageRequest request) {
        return agreementGoodsService.getSupplementarySaleGoodPageList(request);
    }

    @Override
    public List<TempAgreementGoodsBO> getPurchaseGoodsListToSaleAssistant(SalePurchaseGoodsRequest request) {

        return agreementGoodsService.getPurchaseGoodsListToSaleAssistant(request);
    }

    @Override
    public Page<TempAgreementGoodsBO> getTempPurchaseGoodsPageList(AgreementPurchaseGoodsPageRequest request) {
        return agreementGoodsService.getTempPurchaseGoodsPageList(request);
    }

    @Override
    public List<AgreementGoodsDTO> getYearPurchaseGoodsList(AgreementPurchaseGoodsRequest request) {
        return agreementGoodsService.getYearPurchaseGoodsList(request);
    }

    @Override
    public List<AgreementGoodsDTO> getTempPurchaseGoodsList(AgreementPurchaseGoodsRequest request) {
        return agreementGoodsService.getTempPurchaseGoodsList(request);
    }

    @Override
    public List<AgreementGoodsDTO> getTempPurchaseGoodsByDistributionList(AgreementPurchaseGoodsRequest request) {
        return agreementGoodsService.getTempPurchaseGoodsByDistributionList(request);
    }

    @Override
    public Long getAgreementIdByPurchaseGoodsList(Long buyerEid, Long goodsId) {
        return agreementGoodsService.getAgreementIdByPurchaseGoodsList(buyerEid,goodsId);
    }

    @Override
    public List<Long> getAgreementIdsByPurchaseGoodsList(Long buyerEid, List<Long> goodsIds) {
        return agreementGoodsService.getAgreementIdsByPurchaseGoodsList(buyerEid,goodsIds);
    }

//	@Override
//	public Boolean addAgreementGoods(AddAgreementGoodsRequest request) {
//		return agreementGoodsService.addAgreementGoods(request);
//	}

}
