package com.yiling.goods.medicine.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsStatusRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.UpdatePopGoodsRequest;
import com.yiling.goods.medicine.entity.PopGoodsDO;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;

/**
 * <p>
 * pop商品表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-20
 */
public interface PopGoodsService extends BaseService<PopGoodsDO> {

    /**
     * 根据相关参数分页查询供应商列表
     *
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
     * 根据商品状态查询各自商品数量
     *
     * @param eidList
     * @return
     */
    List<QueryStatusCountBO> queryPopStatusCountList(List<Long> eidList);


    /**
     * 根据商品条件查询各自商品状态数量
     *
     * @param request
     * @return
     */
    List<QueryStatusCountBO> queryPopStatusCountListByCondition(QueryGoodsPageListRequest request);

    /**
     * 根据企业ID和内码获取商品信息
     *
     * @param eid
     * @param inSn
     * @return
     */
    GoodsInfoDTO findGoodsByInSnAndEid(Long eid, String inSn);

    /**
     *
     * @param goodsId
     * @return
     */
    GoodsInfoDTO queryInfo(Long goodsId);

    /**
     *
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
     * 获取POP自己商品信息
     * @param goodsIds
     * @return
     */
    List<PopGoodsDTO> getPopGoodsListByGoodsIds(List<Long> goodsIds);

    /**
     * 通过商品Id获取B2b商品信息
     * @param goodsId
     * @return
     */
    PopGoodsDTO getPopGoodsByGoodsId(Long goodsId);

    /**
     * 修改B2b商品信息
     * @param request
     * @return
     */
    Boolean updatePopGoods(UpdatePopGoodsRequest request);
    /**
     * 修改状态
     */
    Boolean updateGoodsStatus(BatchUpdateGoodsStatusRequest request);

    /**
     * 修改pop产品线状态
     * @param request
     * @return
     */
    Boolean updatePopLineStatus(BatchUpdateGoodsStatusRequest request);

    /**
     * 企业pop商品计数
     * @param eid
     * @return
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
    List<GoodsListItemBO> findGoodsBySpecificationIdListAndEidList(List<Long> specificationIdList,List<Long> eidList, GoodsStatusEnum goodsStatusEnum);

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
