package com.yiling.goods.medicine.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;

/**
 * @author: shuang.zhang
 * @date: 2021/11/4
 */
public interface PopGoodsApi {
    /**
     * 根据相关参数分页查询供应商列表
     * @param request
     * @return
     */
    Page<GoodsListItemBO> queryPopGoodsPageList(QueryGoodsPageListRequest request);


    /**
     * 根据相关参数分页查询供应商列表
     *
     * @param request
     * @return
     */
    List<GoodsListItemBO> queryPopGoodsList(QueryGoodsPageListRequest request);

    /**
     * 根据商品条件查询各自商品数量
     * @param request
     * @return
     */
    List<QueryStatusCountBO> queryPopStatusCountListByCondition(QueryGoodsPageListRequest request);


    /**
     * 根据商品状态查询各自商品数量
     * @param eidList
     * @return
     */
    List<QueryStatusCountBO> queryPopStatusCountList(List<Long> eidList);

    /**
     * 通过商业公司和内码查询商品信息
     * @param eid
     * @param inSn
     * @return
     */
    GoodsInfoDTO findGoodsByInSnAndEid(Long eid,String inSn);

    /**
     * 根据goodsId查询商品基础信息，商品封面图
     * @param goodsId
     * @return
     */
    GoodsInfoDTO queryInfo(Long goodsId);

    /**
     * 根据goodsIds批量查询 商品基础信息，商品封面图
     * @param goodsIds
     * @return
     */
    List<GoodsInfoDTO> batchQueryInfo(List<Long> goodsIds);

    /**
     * goodsIds查询pop商品信息
     * @param goodsIds
     * @return
     */
    List<GoodsInfoDTO> batchQueryPopGoods(List<Long> goodsIds);

    /**
     * 商品id
     * @param goodsId
     * @return
     */
    PopGoodsDTO getPopGoodsByGoodsId(Long goodsId);

    /**
     * 商品id
     * @param goodsIds
     * @return
     */
    List<PopGoodsDTO> getPopGoodsListByGoodsIds(List<Long> goodsIds);

    /**
     * 获取企业商品计数
     */
    EnterpriseGoodsCountBO getGoodsCountByEid(Long eid);

    /**
     * 根据规格id和 Eids获取pop商品
     * @param eids
     * @param specificationId
     * @return
     */
    List<GoodsListItemBO> findGoodsBySpecificationIdAndEids(Long specificationId,List<Long> eids);

    /**
     * 根据规格id列表和 Eid列表获取pop商品
     * @param eidList
     * @param specificationIdList
     * @param goodsStatusEnum
     * @return
     */
    List<GoodsListItemBO> findGoodsBySpecificationIdListAndEidList(List<Long> specificationIdList, List<Long> eidList, GoodsStatusEnum goodsStatusEnum);

    /**
     * 根据相关参数查询pop商品
     * @param request
     * @return
     */
    List<PopGoodsDTO> queryPopGoods(QueryGoodsPageListRequest request);

    /**
     * 根据相关参数查询pop商品
     * @param request
     * @return
     */
    Page<PopGoodsDTO> queryPopGoodsPage(QueryGoodsPageListRequest request);
}
