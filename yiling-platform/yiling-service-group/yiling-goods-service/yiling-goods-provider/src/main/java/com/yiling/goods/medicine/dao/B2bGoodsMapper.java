package com.yiling.goods.medicine.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.medicine.bo.ChoicenessGoodsBO;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QuerySellSpecificationsGoodsIdBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.bo.SpecificationPriceBO;
import com.yiling.goods.medicine.dto.request.QueryChoicenessGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QuerySaleSpecificationPageListRequest;
import com.yiling.goods.medicine.entity.B2bGoodsDO;

/**
 * <p>
 * b2b商品表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-20
 */
@Repository
public interface B2bGoodsMapper extends BaseMapper<B2bGoodsDO> {

    /**
     * 根据搜索条件分页检索供应商商品
     * @param page
     * @param request
     * @return
     */
    Page<GoodsListItemBO> queryB2bGoodsPageList(Page<GoodsListItemBO> page, @Param("request") QueryGoodsPageListRequest request);

    /**
     * 条件查询B2b商品列表
     * @param request
     * @return
     */
    List<GoodsListItemBO> queryB2bGoodsList(@Param("request") QueryGoodsPageListRequest request);

    /**
     * 根据商品状态查询各自商品数量
     * @param eidList
     * @return
     */
    List<QueryStatusCountBO> queryB2bStatusCountList(@Param("eidList")List<Long> eidList);

    /**
     * 根据商品的查询条件统计商品数量
     * @param request
     * @return
     */
    List<QueryStatusCountBO> queryB2bStatusCountListByCondition(@Param("request") QueryGoodsPageListRequest request);

    /**
     * 根据商品的eids集合查询销量前limi的数据
     * @param eids
     * @param limit
     * @return 商品Id
     */
    List<Long> getB2bGoodsSaleTopLimit(@Param("eids") List<Long> eids, @Param("limit") Integer limit);

    /**
     * 从传入eid内查询存在上架商品的企业
     * @param eids
     * @return
     */
    List<Long> getEidListBySaleGoods(@Param("eids") List<Long> eids);

    /**
     * 通过销售规格ID查询销售的商业公司
     * @param sellSpecificationsIds
     * @param eids
     * @return
     */
    List<QuerySellSpecificationsGoodsIdBO> getSellerGoodsIdsBySellSpecificationsIds(@Param("sellSpecificationsIds") List<Long> sellSpecificationsIds, @Param("eids")List<Long> eids);

    /**
     * 通过以岭售卖规格Id集合，可以采购的商业公司Eid,购买者Eid
     * @param sellSpecificationsIds
     * @param includeEids
     * @param buyerEid
     * @return
     */
    List<Long> getIdsBySpecificationsIdsAndIncludeEidsAndCustomerEid(@Param("sellSpecificationsIds") List<Long> sellSpecificationsIds, @Param("includeEids")List<Long> includeEids,@Param("buyerEid")Long buyerEid);

    /**
     * 通过购买企业ID和
     * @param page
     * @param request
     * @return
     */
    Page<ChoicenessGoodsBO> getChoicenessByCustomerAndSellSpecificationsId(Page<ChoicenessGoodsBO> page, @Param("request")QueryChoicenessGoodsPageListRequest request);

    /**
     * 通过eid查询b2b商品品和规格数量
     * @param eid
     * @return
     */
    EnterpriseGoodsCountBO getGoodsCountByEid(@Param("eid")Long eid);


    /**
     * 通过eidList查询b2b商品品和规格数量
     * @param eidList
     * @return
     */
    List<EnterpriseGoodsCountBO> getGoodsCountByEidList(@Param("eidList")List<Long> eidList);

    /**
     * 规格id获取最低价
     * @param sellSpecificationsIds
     * @return
     */
    List<SpecificationPriceBO> getMinPriceBySpecificationsIds(@Param("sellSpecificationsIds")List<Long> sellSpecificationsIds);

    /**
     * 获取瀑布流商品分页
     * @param request
     * @return
     */
    Page<Long> queryWaterfallSpecificationPage(Page<GoodsListItemBO> page, @Param("request") QuerySaleSpecificationPageListRequest request);

    /**
     * 规格id查询有库存的商品
     * @param sellSpecificationsId
     * @return
     */
    List<Long> getInStockGoodsBySpecId(@Param("sellSpecificationsId")Long sellSpecificationsId);


    /**
     * 获取规格下配送商商品id
     * @param request
     * @return
     */
    Page<ChoicenessGoodsBO> queryDistributorGoodsBySpec(Page<ChoicenessGoodsBO> page, @Param("request") QueryChoicenessGoodsPageListRequest request);
}
