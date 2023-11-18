package com.yiling.goods.medicine.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.bo.ChoicenessGoodsBO;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.B2bGoodsDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsStatusRequest;
import com.yiling.goods.medicine.dto.request.QueryChoicenessGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QuerySaleSpecificationPageListRequest;
import com.yiling.goods.medicine.dto.request.UpdateB2bGoodsRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsStatusByEidRequest;
import com.yiling.goods.medicine.entity.B2bGoodsDO;

/**
 * <p>
 * b2b商品表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-20
 */
public interface B2bGoodsService extends BaseService<B2bGoodsDO> {

    Map<Long, List<GoodsDTO>> getShopGoodsByEidAndLimit(List<Long> eidList, Integer limit);

    /**
     * 根据相关参数分页查询供应商列表
     * @param request
     * @return
     */
    Page<GoodsListItemBO> queryB2bGoodsPageList(QueryGoodsPageListRequest request);


    List<QueryStatusCountBO> queryB2bStatusCountList(List<Long> eidList);

    List<QueryStatusCountBO> queryB2bStatusCountListByCondition(QueryGoodsPageListRequest request);

    GoodsInfoDTO queryInfo(Long goodsId);

    List<GoodsInfoDTO> batchQueryInfo(List<Long> goodsIds);
    /**
     * 获取B2B自己商品信息
     * @param goodsIds
     * @return
     */
    List<B2bGoodsDTO> getB2bGoodsListByGoodsIds(List<Long> goodsIds);


    /**
     * 通过商品Id获取B2b商品信息
     * @param goodsId
     * @return
     */
    B2bGoodsDTO getB2bGoodsByGoodsId(Long goodsId);


    /**
     * 修改B2b商品信息
     * @param request
     * @return
     */
    Boolean updateB2bGoods(UpdateB2bGoodsRequest request);

    /**
     * 修改状态
     * @param request
     * @return
     */
    Boolean updateGoodsStatus(BatchUpdateGoodsStatusRequest request);

    /**
     * 修改b2b产品线状态
     * @param request
     * @return
     */
    Boolean updateB2bLineStatus(BatchUpdateGoodsStatusRequest request);

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
     * 获取B2B商品个数
     * @param eids
     * @return
     */
    Long countB2bGoodsByEids(List<Long> eids);

    /**
     * 查询某一个商品规格有多个供应商goodsId
     * @param sellSpecificationsIds
     * @return key=商品规格Id，value=供应商数量
     */
    Map<Long, List<Long>> getSellerGoodsIdsBySellSpecificationsIds(List<Long> sellSpecificationsIds, List<Long> eids);


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
     * 店铺关闭下架商品信息
     * @param updateGoodsStatusByEidRequest
     * @return
     */
    boolean updateB2bStatusByEid(UpdateGoodsStatusByEidRequest updateGoodsStatusByEidRequest);

    /**
     * 企业b2b商品计数
     * @param eid
     * @return
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
