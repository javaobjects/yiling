package com.yiling.user.agreement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.agreement.bo.AgreementGoodsPurchaseRelationBO;
import com.yiling.user.agreement.bo.SupplementaryAgreementGoodsBO;
import com.yiling.user.agreement.bo.TempAgreementGoodsBO;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsPageRequest;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.agreement.dto.request.SalePurchaseGoodsRequest;
import com.yiling.user.agreement.entity.AgreementGoodsDO;

/**
 * <p>
 * 协议商品表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
@Repository
public interface AgreementGoodsMapper extends BaseMapper<AgreementGoodsDO> {

    /**
     * 根据搜索条件分页检索供应商商品
     * @param goodsIds
     * @return
     */
    List<AgreementGoodsPurchaseRelationBO> getBuyerGatherByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 查询采购商可以采购的年度协议商品信息
     * @param page
     * @param request
     * @return
     */
    Page<AgreementGoodsDO> getYearPurchaseGoodsPageList(Page<AgreementGoodsDO> page, @Param("request") AgreementPurchaseGoodsPageRequest request);


    /**
     * 获取补充协议在售商品
     * @param request
     * @return
     */
    Page<SupplementaryAgreementGoodsBO>  getSupplementarySaleGoodPageList(Page<AgreementGoodsDO> page,@Param("request")AgreementPurchaseGoodsPageRequest request);

    /**
     * 销售助手，查询采购商可以采购的年度协议商品信息
     *
     * @param request
     * @return
     */
    List<TempAgreementGoodsBO> getPurchaseGoodsListToSaleAssistant(@Param("request") SalePurchaseGoodsRequest request);


    /**
     * 查询采购商可以采购的补充协议商品信息
     * @param page
     * @param request
     * @return
     */
    Page<TempAgreementGoodsBO> getTempPurchaseGoodsPageList(Page<AgreementGoodsDO> page, @Param("request") AgreementPurchaseGoodsPageRequest request);

    /**
     * 通过名称或者批次号获取可以采购的商品
     * @param request
     * @return
     */
    List<AgreementGoodsDO> getYearPurchaseGoodsList(@Param("request")AgreementPurchaseGoodsRequest request);

    /**
     * 通过名称或者批次号获取可以采购的商品
     * @param request
     * @return
     */
    List<AgreementGoodsDO> getTempPurchaseGoodsList(@Param("request")AgreementPurchaseGoodsRequest request);

    /**
     * 通过名称或者批次号获取可以采购的商品
     * @param request
     * @return
     */
    List<AgreementGoodsDO> getTempPurchaseGoodsByDistributionList(@Param("request")AgreementPurchaseGoodsRequest request);

    /**
     * 通过名称或者批次号获取可以采购的商品
     * @param buyerEid
     * @param goodsIds
     * @return
     */
    List<Long> getAgreementIdsByPurchaseGoodsList(@Param("buyerEid")Long buyerEid, @Param("goodsIds")List<Long> goodsIds);
}
