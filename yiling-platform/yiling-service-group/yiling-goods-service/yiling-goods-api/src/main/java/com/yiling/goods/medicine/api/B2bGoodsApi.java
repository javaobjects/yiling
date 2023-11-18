package com.yiling.goods.medicine.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.medicine.bo.ChoicenessGoodsBO;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.B2bGoodsDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.QueryChoicenessGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QuerySaleSpecificationPageListRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsStatusByEidRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/11/4
 */
public interface B2bGoodsApi {

    /**
     * 获取店铺集合里面销量前4的商品信息
     *
     * @param eidList
     * @param limit
     * @return
     */
    Map<Long,List<GoodsDTO>> getShopGoodsByEidAndLimit(List<Long> eidList,Integer limit);

    /**
     * 获取B2B销售商品为前30
     * @param eids
     * @param limit
     * @return
     */
    List<GoodsDTO> getB2bGoodsSaleTopLimit(List<Long> eids, Integer limit);

    /**
     * 获取存在上架商品的企业
     * @param eids
     * @return
     */
    List<Long> getEidListBySaleGoods(List<Long> eids);

    /**
     * 根据相关参数分页查询供应商列表
     * @param request
     * @return
     */
    Page<GoodsListItemBO> queryB2bGoodsPageList(QueryGoodsPageListRequest request);

    /**
     * 根据商品条件查询各自商品数量
     * @param request
     * @return
     */
    List<QueryStatusCountBO> queryB2bStatusCountListByCondition(QueryGoodsPageListRequest request);

    /**
     * 根据商品状态查询各自商品数量
     * @param eidList
     * @return
     */
    List<QueryStatusCountBO> queryB2bStatusCountList(List<Long> eidList);


    /**
     * 根据goodsIds批量查询 商品基础信息，商品封面图
     * @param goodsIds
     * @return
     */
    List<GoodsInfoDTO> batchQueryInfo(List<Long> goodsIds);


    /**
     * 根据goodsId查询商品基础信息，商品封面图
     * @param goodsId
     * @return
     */
    GoodsInfoDTO queryInfo(Long goodsId);

    /**
     * 商品id
     * @param goodsId
     * @return
     */
    B2bGoodsDTO getB2bGoodsByGoodsId(Long goodsId);

    /**
     * 商品id集合
     * @param goodsIds
     * @return
     */
    List<B2bGoodsDTO> getB2bGoodsListByGoodsIds(List<Long> goodsIds);



    /**
     * 获取B2B商品个数
     * @param eids
     * @return
     */
    Long countB2bGoodsByEids(List<Long> eids);

    /**
     * 查询某一个销售规格有多个供应商销售
     * @param sellSpecificationsIds
     * @return key=销售规格Id，value=供应商数量
     */
    Map<Long,List<Long>> getSellerGoodsIdsBySellSpecificationsIds(List<Long> sellSpecificationsIds, List<Long> eids);

    /**
     * 查询某一个规格有多个供应商eid
     * @param sellSpecificationsIds
     * @return key=商品规格Id，value=供应商数量
     */
    Map<Long, List<Long>> getSellerEidsBySellSpecificationsIds(List<Long> sellSpecificationsIds, List<Long> eids);

    /**
     * 获取商业公司销售以岭品规Id集合
     * @param sellSpecificationsIds
     * @param includeEids
     * @param buyerEid
     * @return
     */
    List<Long> getIdsBySpecificationsIdsAndIncludeEidsAndCustomerEid(List<Long> sellSpecificationsIds,List<Long> includeEids,Long buyerEid);

    /**
     * 精选商品供应商商品列表
     * @param request
     * @return
     */
    Page<ChoicenessGoodsBO> getChoicenessByCustomerAndSellSpecificationsId(QueryChoicenessGoodsPageListRequest request);

    /**
     * 获取销售包装库存IdList集合
     * @param goodsId
     * @return
     */
    List<Long> getB2bInventoryByGoodsId(Long goodsId);

    /**
     * 关闭店铺需要下架商品
     * @return
     */
    boolean updateB2bStatusByEid(UpdateGoodsStatusByEidRequest request);

    /**
     * 获取企业商品计数
     */
    EnterpriseGoodsCountBO getGoodsCountByEid(Long eid);

    /**
     * 通过eidList查询b2b商品品和规格数量
     * @param eidList
     * @return
     */
    Map<Long,EnterpriseGoodsCountBO> getGoodsCountByEidList(List<Long> eidList);

    /**
     * 规格id和企业id 查询b2b商品
     * @param request
     * @return
     */
    List<GoodsListItemBO> getB2bGoodsBySellSpecificationsIdsAndEids(QueryGoodsPageListRequest request);

    /**
     * 规格id 查询上架商品最低价
     * @param sellSpecificationsIds
     * @return
     */
    Map<Long, BigDecimal> getMinPriceBySpecificationsIds(List<Long> sellSpecificationsIds);

    /**
     * 获取瀑布流商品分页
     * @param request
     * @return
     */
    Page<GoodsListItemBO> queryWaterfallSpecificationPage(QuerySaleSpecificationPageListRequest request);

    /**
     * 规格id查询有库存的商品
     * @param sellSpecificationsId
     * @return
     */
    List<Long> getInStockGoodsBySpecId(Long sellSpecificationsId);

    /**
     * 获取规格下配送商商品id
     * @param request
     * @return
     */
    Page<ChoicenessGoodsBO> queryDistributorGoodsBySpec(QueryChoicenessGoodsPageListRequest request);
}
