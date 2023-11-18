package com.yiling.user.agreement.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.bo.AgreementGoodsPurchaseRelationBO;
import com.yiling.user.agreement.bo.SupplementaryAgreementGoodsBO;
import com.yiling.user.agreement.bo.TempAgreementGoodsBO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.AgreementGoodsDetailsPageRequest;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsPageRequest;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreement.dto.request.SalePurchaseGoodsRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementGoodsRequest;
import com.yiling.user.agreement.entity.AgreementGoodsDO;

/**
 * <p>
 * 协议商品表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
public interface AgreementGoodsService extends BaseService<AgreementGoodsDO> {

    /**
     * 协议商品列表
     * @param request
     * @return
     */
    Page<AgreementGoodsDTO> agreementGoodsListPage(QueryAgreementGoodsPageRequest request);

    /**
     * 根据AgreementId查询分页商品
     * @param request
     * @return
     */
    Page<AgreementGoodsDTO> getAgreementGoodsByAgreementIdPage(AgreementGoodsDetailsPageRequest request);

    /**
     * 根据AgreementId查询所有协议商品
     * @param agreementId
     * @return
     */
    List<AgreementGoodsDTO> getAgreementGoodsAgreementIdByList(Long agreementId);

    /**
     * 根据AgreementIds批量查询所有协议商品
     * @param agreementIds
     * @return
     */
    Map<Long, List<AgreementGoodsDTO>> getAgreementGoodsListByAgreementIds(List<Long> agreementIds);

    /**
     * 通过协议id删除协议商品，并且在添加协议商品
     * @param goodsList
     * @param agreementId
     * @param opUserId
     * @return
     */
    Boolean updateByGoodsListAndAgreementId(List<SaveAgreementGoodsRequest> goodsList,Long agreementId,Long opUserId);

    /**
     * 删除临时协议里面的协议商品
     * @param goodsIds
     * @param agreementIds
     * @param opUserId
     * @return
     */
    Boolean deleteByGoodsIdsAndAgreementId(List<Long> goodsIds,List<Long> agreementIds,Long opUserId);

    /**
     * 根据商品集合重新查询可购买的配货商
     * @param goodsIds
     * @return
     */
    List<AgreementGoodsPurchaseRelationBO> getBuyerGatherByGoodsIds(List<Long> goodsIds);

    /**
     * 查询采购商可以采购的年度协议商品信息
     * @param request
     * @return
     */
    Page<AgreementGoodsDTO> getYearPurchaseGoodsPageList(AgreementPurchaseGoodsPageRequest request);

    /**
     * 获取补充协议在售商品
     * @param request
     * @return
     */
    Page<SupplementaryAgreementGoodsBO>  getSupplementarySaleGoodPageList(AgreementPurchaseGoodsPageRequest request);

    /**
     * 销售助手查询可采配送商商品
     * @param request
     * @return
     */
    List<TempAgreementGoodsBO> getPurchaseGoodsListToSaleAssistant(SalePurchaseGoodsRequest request);

    /**
     * 查询采购商可以采购的补充协议商品信息
     * @param request
     * @return
     */
    Page<TempAgreementGoodsBO> getTempPurchaseGoodsPageList(AgreementPurchaseGoodsPageRequest request);

    List<AgreementGoodsDTO> getTempPurchaseGoodsByDistributionList(AgreementPurchaseGoodsRequest request);

    /**
     * 通过名称或者批次号获取可以采购的商品
     * @param request
     * @return
     */
    List<AgreementGoodsDTO> getYearPurchaseGoodsList(AgreementPurchaseGoodsRequest request);

    /**
     * 通过名称或者批次号获取可以采购的商品
     * @param request
     * @return
     */
    List<AgreementGoodsDTO> getTempPurchaseGoodsList(AgreementPurchaseGoodsRequest request);

    /**
     * 通过采购商ID和商品ID返回主协议ID
     * @param buyerEid
     * @param goodsId
     * @return
     */
     Long getAgreementIdByPurchaseGoodsList(Long buyerEid, Long goodsId);

    /**
     * 通过采购商ID和商品ID返回主协议ID
     * @param buyerEid
     * @param goodsIds
     * @return
     */
     List<Long> getAgreementIdsByPurchaseGoodsList(Long buyerEid, List<Long> goodsIds);

//	/**
//	 * 根据协议id和商品信息新增协议商品
//	 * @param request
//	 * @return
//	 */
//	Boolean addAgreementGoods(AddAgreementGoodsRequest request);

}
