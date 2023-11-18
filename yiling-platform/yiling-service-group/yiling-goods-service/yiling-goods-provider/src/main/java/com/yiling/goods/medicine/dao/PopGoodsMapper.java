package com.yiling.goods.medicine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.entity.PopGoodsDO;

/**
 * <p>
 * pop商品表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-20
 */
@Repository
public interface PopGoodsMapper extends BaseMapper<PopGoodsDO> {

    /**
     * 根据搜索条件分页检索供应商商品
     * @param page
     * @param request
     * @return
     */
    Page<GoodsListItemBO> queryPopGoodsPageList(Page<GoodsListItemBO> page, @Param("request") QueryGoodsPageListRequest request);

    /**
     * 根据搜索条件检索供应商商品
     * @param request
     * @return
     */
    List<GoodsListItemBO> queryPopGoodsList(@Param("request") QueryGoodsPageListRequest request);

    /**
     * 根据商品状态查询各自商品数量
     * @param eidList
     * @return
     */
    List<QueryStatusCountBO> queryPopStatusCountList(@Param("eidList")List<Long> eidList);

    /**
     * 根据商品的查询条件统计商品数量
     * @param request
     * @return
     */
    List<QueryStatusCountBO> queryPopStatusCountListByCondition(@Param("request") QueryGoodsPageListRequest request);

    /**
     * 通过eid查询b2b商品种类数量
     * @param eid
     * @return
     */
    Long getStandardCountByEid(@Param("eid")Long eid);

    /**
     * 通过eid查询b2b商品品格数量
     * @param eid
     * @return
     */
    Long getSellSpecificationCountByEid(@Param("eid")Long eid);

    /**
     * 规格id和eids查询pop商品
     * @param specId
     * @param eidList
     */
    List<GoodsListItemBO> queryPopGoodsBySpecificationIdAndEid(@Param("specId")Long specId,@Param("eidList")List<Long> eidList);


    /**
     * 规格id列表和eid列表查询pop商品
     * @param specificationIdList
     * @param eidList
     * @return
     */
    List<GoodsListItemBO> findGoodsBySpecificationIdListAndEidList(@Param("specificationIdList")List<Long> specificationIdList,@Param("eidList")List<Long> eidList,@Param("goodsStatus") Integer goodsStatus);

    /**
     * 根据相关参数查询pop商品
     * @param request
     * @return
     */
    List<PopGoodsDO> queryPopGoods(@Param("request")QueryGoodsPageListRequest request);

    /**
     * 根据相关参数查询pop商品
     * @param request
     * @param page
     * @return
     */
    Page<PopGoodsDO> queryPopGoods(Page page,@Param("request")QueryGoodsPageListRequest request);

}
