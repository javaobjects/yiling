package com.yiling.goods.medicine.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.medicine.bo.CustomerGoodsPriceLimitBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.CustomerPriceLimitDTO;
import com.yiling.goods.medicine.dto.GoodsPriceLimitDTO;
import com.yiling.goods.medicine.dto.request.AddOrDeleteCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.BatchAddCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.DeleteGoodsPriceLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLimitPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPriceLimitPageRequest;
import com.yiling.goods.medicine.dto.request.QueryLimitFlagRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsPriceLimitRequest;
import com.yiling.goods.medicine.dto.request.UpdateCustomerLimitRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/10/25
 */
public interface GoodsLimitPriceApi {

    /**
     * 查询企业的限价标识
     * @param request
     * @return
     */
    List<CustomerPriceLimitDTO> getCustomerLimitFlagByEidAndCustomerEid(QueryLimitFlagRequest request);

    /**
     * 修改客户限价标识
     * @param request
     * @return
     */
    Boolean updateCustomerLimitByEidAndCustomerEid(UpdateCustomerLimitRequest request);

    /**
     * 通过商业公司eid获取推荐标识
     * @param eid
     * @return
     */
    Map<Long,Integer> getRecommendationFlagByCustomerEids(List<Long> customerEids,Long eid);

    /**
     * 添加客户限价商品Id集合
     * @param request
     * @return
     */
    Boolean addCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request);

    /**
     * 添加客户限价商品Id集合
     * @param request
     * @return
     */
    Boolean batchAddCustomerGoodsLimitByCustomerEid(BatchAddCustomerGoodsLimitRequest request);

    /**
     * 移除客户限价商品Id集合
     * @param request
     * @return
     */
    Boolean deleteCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request);

    /**
     * 商品限价分页
     * @param request
     * @return
     */
    Page<GoodsPriceLimitDTO> pageList(QueryGoodsPriceLimitPageRequest request);

    /**
     * 商品限价分页
     * @param request
     * @return
     */
    Page<GoodsListItemBO> pageLimitList(QueryGoodsLimitPageListRequest request);

    /**
     * 添加商品限价条件
     * @param request
     * @return
     */
    Boolean addGoodsPriceLimit(SaveOrUpdateGoodsPriceLimitRequest request);

    /**
     * 添加商品限价条件
     * @param id
     * @return
     */
    GoodsPriceLimitDTO getGoodsPriceLimitById(Long id);

    /**
     * 移除商品限价条件
     * @param request
     * @return
     */
    Boolean deleteGoodsPriceLimit(DeleteGoodsPriceLimitRequest request);

    /**
     * 编辑商品限价条件
     * @param request
     * @return
     */
    Boolean updateGoodsPriceLimit(SaveOrUpdateGoodsPriceLimitRequest request);

    /**
     * 获取商品控销条件列表
     * @param goodsIds
     * @return
     */
    Map<Long,List<GoodsPriceLimitDTO>> listGoodsPriceLimitByGoodsIds(List<Long> goodsIds);

    /**
     * 获取控制客户下面的所有商品信息
     * @param customerEid
     * @return
     */
    CustomerPriceLimitDTO getCustomerPriceLimitByCustomerEid(Long eid,Long customerEid);

    /**
     * 获取控制客户下面的所有商品信息
     * @param customerEid
     * @return
     */
    List<Long> getGoodsIdsByCustomerEid(Long eid,Long customerEid);

    /**
     * 获取商品对应客户是否限价
     * @param gidList
     * @param eid
     * @param buyerEid
     * @return
     */
    List<Long> getIsGoodsPriceByGoodsIdsAndBuyerEid(List<Long> gidList,Long eid, Long buyerEid);

    /**
     * 商品ids和客户ids，获取限价商品
     * @param gidList
     * @param eid
     * @param customerEidList
     * @return
     */
    Map<Long,List<Long>> getLimitByGidsAndCustomerEidsGroupByCustomerEid(List<Long> gidList, List<Long> customerEidList,Long eid);
}
