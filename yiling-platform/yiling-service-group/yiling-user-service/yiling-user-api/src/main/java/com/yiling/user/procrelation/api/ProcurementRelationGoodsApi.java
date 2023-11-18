package com.yiling.user.procrelation.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;
import com.yiling.user.procrelation.dto.ProcRelationRebateResultBO;
import com.yiling.user.procrelation.dto.ProcurementRelationGoodsDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsForProcRelationRequest;
import com.yiling.user.procrelation.dto.request.CalculateProcRelationRebateRequest;
import com.yiling.user.procrelation.dto.request.QueryGoodsByEachOtherRequest;
import com.yiling.user.procrelation.dto.request.QueryRelationGoodsPageRequest;
import com.yiling.user.procrelation.dto.request.QuerySpecByBuyerPageRequest;

/**
 * @author: dexi.yao
 * @date: 2023-05-19
 */
public interface ProcurementRelationGoodsApi {

    /**
     * 保存采购关系商品
     *
     * @param requestList
     * @return
     */
    Boolean saveGoodsForProcRelation(List<AddGoodsForProcRelationRequest> requestList);

    /**
     * 根据购买人分页查询有效以岭商品id
     *
     * @param request
     * @return
     */
    Page<Long> queryGoodsPageByBuyer(QuerySpecByBuyerPageRequest request);

    /**
     * 通过规格id和购买人 查询生效配送商关系
     *
     * @param specIds
     * @param buyerEid
     * @return key 规格id value 配送商eidList
     */
    Map<Long, List<DistributorGoodsBO>> getDistributorBySpecIdAndBuyer(List<Long> specIds, Long buyerEid);

    /**
     * 通过yl商品id和购买人 查询生效配送商关系
     *
     * @param ylGoodsIds
     * @param buyerEid
     * @return key yl商品id value 配送商eidList
     */
    Map<Long, List<DistributorGoodsBO>> getDistributorByYlGoodsIdAndBuyer(List<Long> ylGoodsIds, Long buyerEid);

    /**
     * 获取指定规格内有效规格
     *
     * @param specIds
     * @param buyerEid
     * @return
     */
    List<Long> getSpecBySpecIds(List<Long> specIds, Long buyerEid);

    /**
     * 获取指定以岭商品id内有效以岭商品id
     *
     * @param ylGoodsId
     * @param buyerEid
     * @return
     */
    List<Long> getYlGoodsIdByYlGoodsIds(List<Long> ylGoodsId, Long buyerEid);

    /**
     * 根据采购关系id查询采购关系下的商品
     *
     * @param relationId
     * @return
     */
    List<ProcurementRelationGoodsDTO> queryGoodsByRelationId(Long relationId);

    /**
     * 根据采购关系id查询采购关系下的商品
     *
     * @param request
     * @return
     */
    Page<ProcurementRelationGoodsDTO> queryProcRelationGoodsPage(QueryRelationGoodsPageRequest request);

    /**
     * 根据买卖双方查询可采商品
     *
     * @param request
     * @return
     */
    List<DistributorGoodsBO> queryGoodsListByEachOther(QueryGoodsByEachOtherRequest request);

    /**
     * 根据采购关系计算优惠金额
     * @param request
     * @return
     */
    List<ProcRelationRebateResultBO> calculateRebateForGoods(CalculateProcRelationRebateRequest request);
}
