package com.yiling.goods.medicine.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.medicine.bo.DistributorAgreementGoodsBO;
import com.yiling.goods.medicine.bo.GoodsLineBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.MatchGoodsBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsLogPageListItemDTO;
import com.yiling.goods.medicine.dto.MergeGoodsRequest;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuFullDTO;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsOverSoldRequest;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsStatusRequest;
import com.yiling.goods.medicine.dto.request.MatchGoodsRequest;
import com.yiling.goods.medicine.dto.request.QueryDistributorGoodsRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLogRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.dto.request.UpdateAuditStatusByGoodsIdRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.UpdateShelfLifeRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/5/20
 */
public interface GoodsApi {

    /**
     * 供应商商品新增
     * @param saveOrUpdateGoodsRequest
     * @return
     */
    Long saveGoods(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest);

    /**
     * 编辑商品
     *
     * @param saveOrUpdateGoodsRequest
     * @return
     */
    Long editGoods(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest);

    /**
     * erp保存商品
     * @param saveOrUpdateGoodsRequest
     * @return
     */
    Long saveGoodsByErp(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest);

    /**
     * 开通产品线
     * @param request
     * @return
     */
    Boolean updateGoodsLine(UpdateGoodsLineRequest request);

    /**
     * 修改商品审核状态
     * @param request
     */
    Boolean updateAuditStatusByGoodsId(UpdateAuditStatusByGoodsIdRequest request);

    /**
     * 匹配标准库id以及售卖规格id(0-未匹配任何信息 1-只匹配上standardId 2-匹配上specificationId
     *
     * @param request 通过对象取匹配标准库
     * @return
     */
    MatchGoodsBO matchSellSpecificationsByGoods(MatchGoodsRequest request);

    /**
     * 根据相关参数分页查询供应商列表
     * @param request
     * @return
     */
    Page<GoodsListItemBO> queryPageListGoods(QueryGoodsPageListRequest request);

    /**SpecificationsId
     * 通过供应商Id获取商品Id集合 (只包含审核通过)
     * @param eid
     * @return
     */
    List<Long> getGoodsIdsByEid(Long eid);

    /**
     * 通过供应商Id获取商品个数
     * @param eid
     * @return
     */
    List<QueryStatusCountBO> getCountByEid(Long eid);

    /**
     * 获取商品信息
     * @param eid
     * @return
     */
    List<GoodsDTO> getGoodsListByEid(Long eid);

    /**
     * 通过商业公司EId和内码获取审核通过的商品信息
     * @param eid
     * @param inSn
     * @param goodsLine
     * @return
     */
    GoodsDTO findGoodsAuditPassByInSnAndEidAndGoodsLine(Long eid, String inSn,Integer goodsLine);

    /**
     * 通过商业公司EId和内码获取商品信息
     * @param eid
     * @param inSn
     * @param goodsLine
     * @return
     */
    GoodsDTO findGoodsByInSnAndEidAndGoodsLine(Long eid, String inSn, Integer goodsLine);

    /**
     * 根据goodsId查询商品基础信息，商品封面图
     * @param goodsId
     * @return
     */
    GoodsDTO queryInfo(Long goodsId);

    /**
     * 商品skuId获取商品
     * @param skuId
     * @return
     */
    GoodsSkuInfoDTO getGoodsSkuInfoById(Long skuId);

    /**
     * 根据goodsIds批量查询 商品基础信息，商品封面图
     * @param goodsIds
     * @return
     */
    List<GoodsDTO> batchQueryInfo(List<Long> goodsIds);

    /**
     * 商品skuId获取商品
     * @param skuIds
     * @return
     */
    List<GoodsSkuInfoDTO> batchQueryInfoBySkuIds(List<Long> skuIds);

    /**
     * 根据goodsId查询商品基础信息,商品封面图，standardGoodsAllInfo(标准库信息，说明书信息,规格信息，图片信息）
     * @param goodsId 供应商商品ID
     * @return
     */
    GoodsFullDTO queryFullInfo(Long goodsId);

    /**
     * 根据goodsId查询商品基础信息,商品封面图，standardGoodsAllInfo(标准库信息，说明书信息,规格信息，图片信息）
     * @param skuId 供应商商品ID
     * @return
     */
    GoodsSkuFullDTO queryFullInfoBySkuId(Long skuId);

    /**
     * 根据goodsIds查询商品基础信息 ,商品封面图片, 标准库商品基本信息（standardGoods）
     * @param goodsIds 供应商商品ID集合
     * @return
     */
    List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasic(List<Long> goodsIds);

    /**
     * 根据goodsIds查询商品基础信息 ,商品封面图片, 标准库商品基本信息（standardGoods）
     * @param skuIds 供应商商品ID集合
     * @return
     */
    List<GoodsSkuStandardBasicDTO> batchQueryStandardGoodsBasicBySkuIds(List<Long> skuIds);

    /**
     * 通过供应商goodsIds集合获取封面信息
     *
     * @param goodsIds
     * @return
     */
    Map<Long,String> getPictureUrlMapByGoodsIds(List<Long> goodsIds);

    /**
     * 根据以岭goodsId和配送商id查询商品基础信息
     * @param requestList
     * @return
     */
    Map<Long,List<GoodsDTO>> batchQueryDistributorGoodsInfo(List<QueryDistributorGoodsRequest> requestList);

    /**
     * 通过以岭商品和配送商ID集合查询库存信息
     *
     * @param goodsId
     * @param distributorEids
     * @return key=以岭商品IDID value=配送商商品Id
     */
    Map<Long, Long> getDistributorGoodsIdMapByYlGoodsId(List<Long> distributorEids,Long goodsId);

    /**
     * 通过以岭id和配送批量查询配送商品库存信息
     *
     * @param goodsIds
     * @param distributorEid
     * @return key=以岭商品ID value=配送商商品Id
     */
    Map<Long,Long> getDistributorGoodsMapByDistributorEid(List<Long> goodsIds, Long distributorEid);

    /**
     * 根据协议以岭商品分页查询配送商商品
     * @param goodsAgreementMap
     * @param distributorEid
     * @param current
     * @param size
     * @return
     */
    Page<DistributorAgreementGoodsBO> getDistributorGoodsByrAgreementGoodsPageList(Map<Long,String> goodsAgreementMap, Long distributorEid,int current,int size);

    /**
     * 根据售卖规格ID列表和商业公司id查询商品信息
     * @param sellSpecificationsIdList
     * @param eid
     * @return
     */
    List<GoodsDTO> findGoodsBySellSpecificationsIdAndEid(List<Long> sellSpecificationsIdList, List<Long> eid);

    /**
     * 批量修改商品状态
     * @param request
     * @return
     */
	Boolean batchUpdateGoodsStatus(BatchUpdateGoodsStatusRequest request);

    /**
     * 批量修改商品超卖状态
     * @param request
     * @return
     */
    Boolean batchUpdateGoodsOverSold(BatchUpdateGoodsOverSoldRequest request);

	/**
     * 根据相关参数查询商品日志分页列表
     * @param request
     * @return
     */
    Page<GoodsLogPageListItemDTO> queryGoodsLogPageList(QueryGoodsLogRequest request);

    /**
     * 根据商品ids获取产品线信息
     * @param goodsIdList
     * @return
     */
    List<GoodsLineBO> getGoodsLineByGoodsIds(List<Long> goodsIdList);

    /**
     * 根据商品id获取销售包装信息
     * @param goodsId
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGoodsId(Long goodsId);


    /**
     * 根据goodsId和状态获取sku
     * @param goodsId
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGoodsIdAndStatus(Long goodsId,List<Integer> statusList);

    /**
     * 根据商品id获取销售包装信息
     * @param goodsIds
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGoodsIds(List<Long> goodsIds);

    /**
     * 根据goodsIds和状态获取销售包装信息
     * @param goodsIds
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGoodsIdsAndStatus(List<Long> goodsIds,List<Integer> statusList);
    /**
     * 商品skuId获取商品
     * @param skuIds
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByIds(List<Long> skuIds);
    /**
     * 判断是否在待审核里面
     * @param goodsLine
     * @param goodsId
     * @return
     */
    Boolean isWaitSetGoodsStatus(Integer goodsLine, Long goodsId);

    /**
     * 通过销售规格ID和企业ID查询商品ID
     * @param specId
     * @param eid
     * @return
     */
    Long queryInfoBySpecIdAndEid(Long specId,Long eid);

    /**
     * 商品Id集合获取以岭商品ID
     * @param goodsIds
     * @param yilingEids
     * @return key=goodsId value=0(不是以岭品，大于0就是以岭商品ID)
     */
    Map<Long,Long> getYilingGoodsIdByGoodsIdAndYilingEids(List<Long> goodsIds,List<Long> yilingEids);

    /**
     * 通过内码和企业id，更新sku状态
     * @param eid
     * @param inSn
     * @return
     */
    Boolean updateSkuStatusByEidAndInSn(Long eid,String inSn,Integer status,Long updater);

    /**
     * 商品合并导入
     * @param mergeGoodsRequest
     * @return
     */
    String goodsMerge(MergeGoodsRequest mergeGoodsRequest);

    /**
     * 通过商业的eid和内码查询对应以岭品
     * @param eid
     * @param inSn
     * @param yilingEids
     * @return
     */
    GoodsDTO getYlGoodsByEidAndInSn(Long eid, String inSn,List<Long> yilingEids);

    /**
     * skuId获取sku对象
     * @return
     */
    GoodsSkuDTO getGoodsSkuById(Long skuId);

    /**
     * 库存id 获取订阅库存列表
     * @param skuId
     * @return
     */
    List<InventorySubscriptionDTO> getInventoryDetailByInventoryId(Long skuId);

    /**
     * 更新保质期
     * @param request
     * @return
     */
    Boolean updateShelfLife(UpdateShelfLifeRequest request);
}
