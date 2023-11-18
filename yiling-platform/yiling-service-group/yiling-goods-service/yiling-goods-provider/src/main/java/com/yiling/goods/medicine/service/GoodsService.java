package com.yiling.goods.medicine.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.medicine.bo.DistributorAgreementGoodsBO;
import com.yiling.goods.medicine.bo.GoodsLineBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.MatchGoodsBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsLogPageListItemDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuFullDTO;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsOverSoldRequest;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsStatusRequest;
import com.yiling.goods.medicine.dto.MergeGoodsRequest;
import com.yiling.goods.medicine.dto.request.MatchGoodsRequest;
import com.yiling.goods.medicine.dto.request.QueryDistributorGoodsRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLogRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.dto.request.UpdateAuditStatusByGoodsIdRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsRequest;
import com.yiling.goods.medicine.dto.request.UpdateShelfLifeRequest;
import com.yiling.goods.medicine.entity.GoodsDO;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface GoodsService extends BaseService<GoodsDO> {

    /**
     * 供应商商品数据新增或者修改（包含记录商品的修改日志 上下架 库存 价格）
     *
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
     * Erp同步方法
     *
     * @param saveOrUpdateGoodsRequest
     * @return
     */
    Long saveGoodsByErp(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest);

    /**
     * 修改商品状态信息
     * @param updateGoodsRequest
     * @return
     */
     Boolean updateGoods(UpdateGoodsRequest updateGoodsRequest);

    /**
     * 修改商品审核状态
     * @param request
     */
    Boolean updateAuditStatusByGoodsId(UpdateAuditStatusByGoodsIdRequest request);

    /**
     * 修改产品线
     * @param request
     * @return
     */
    Boolean updateGoodsLine(UpdateGoodsLineRequest request);

    /**
     * 商品信息匹配标准库商品方法
     *
     * @param request
     * @return
     */
    MatchGoodsBO matchSellSpecificationsByGoods(MatchGoodsRequest request);

    /**
     * 根据相关参数分页查询供应商列表
     *
     * @param request
     * @return
     */
    Page<GoodsListItemBO> queryPageListGoods(QueryGoodsPageListRequest request);

    /**
     * 根据goodsIds 批量查询商品基本信息及库存
     *
     * @param goodsIds
     * @return
     */
    List<GoodsDTO> batchQueryInfo(List<Long> goodsIds);

    /**
     * 根据goodsIds 批量查询商品基本信息及库存
     *
     * @param skuIds
     * @return
     */
    List<GoodsSkuInfoDTO> batchQueryInfoBySkuIds(List<Long> skuIds);

    /**
     * 通过供应商Id获取商品Id集合
     *
     * @param eid 供应商Id
     * @return
     */
    List<Long> getGoodsIdsByEid(Long eid);

    /**
     * 通过企业Id获取商品总数
     * @param eid
     * @return
     */
    List<QueryStatusCountBO> getCountByEid(Long eid);

    /**
     * 获取商品信息
     * @param eid
     * @return
     */
    List<GoodsDO> getGoodsListByEid(Long eid);

    /**
     * 通过商品内码和eid获取已经审核通过的商品信息
     * @param eid
     * @param inSn
     * @param goodsLine
     * @return
     */
    GoodsDTO findGoodsAuditPassByInSnAndEidAndGoodsLine(Long eid, String inSn, Integer goodsLine);

    /**
     * 通过商品内码和eid获取商品信息
     * @param eid
     * @param inSn
     * @param goodsLine
     * @return
     */
    GoodsDTO findGoodsByInSnAndEidAndGoodsLine(Long eid, String inSn, Integer goodsLine);

    /**
     * 根据goodsId查询商品基本信息及库存
     *
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
     * 根据goodsId查询商品基础信息，库存，标准库分类信息，标准库信息(如果商品有售卖规格ID，需要判断规格ID对应是否有图片)
     *
     * @param goodsId
     * @return
     */
    GoodsFullDTO queryFullInfo(Long goodsId);

    /**
     * 根据goodsId查询商品基础信息，库存，标准库分类信息，标准库信息(如果商品有售卖规格ID，需要判断规格ID对应是否有图片)
     *
     * @param skuId
     * @return
     */
    GoodsSkuFullDTO queryFullInfoBySkuId(Long skuId);

    /**
     * 根据goodsId查询商品基础信息，库存，标准库分类信息，标准库信息
     *
     * @param goodsIds
     * @return
     */
    List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasic(List<Long> goodsIds);

    /**
     * 根据goodsId查询商品基础信息，库存，标准库分类信息，标准库信息
     *
     * @param skuIds
     * @return
     */
    List<GoodsSkuStandardBasicDTO> batchQueryStandardGoodsBasicBySkuIds(List<Long> skuIds);

    /**
     * 通过标准库ID或者售卖规格ID获取默认图片
     *
     * @param standardId
     * @param sellSpecificationsId
     * @return
     */
    String getDefaultUrl(Long standardId, Long sellSpecificationsId);

    /**
     * 通过标准图片获取默认图片
     * @param standardGoodsPicDOList
     * @param sellSpecificationsId
     * @return
     */
    String getDefaultUrlByStandardGoodsPicList(List<StandardGoodsPicDO> standardGoodsPicDOList, Long sellSpecificationsId);

    /**
     * 根据以岭goodsId和配送商id查询商品基础信息
     * @param requestList
     * @return
     */
    Map<Long, List<GoodsDTO>> batchQueryDistributorGoodsInfo(List<QueryDistributorGoodsRequest> requestList);

    /**
     * 通过供应商goodsIds集合获取封面信息
     *
     * @param goodsIds
     * @return
     */
    Map<Long, String> getPictureUrlMapByGoodsIds(List<Long> goodsIds);

    /**
     * 根据售卖规格ID和eid查询商品ID（只能保证唯一）
     *
     * @param sellSpecificationsId
     * @param eid
     * @return
     */
    Long queryInfoBySpecIdAndEid(Long sellSpecificationsId, Long eid);

    /**
     * 根据售卖规格ID和eid集合查询商品ID（只能保证唯一）
     *
     * @param sellSpecificationsId
     * @param eids
     * @return
     */
    Map<Long, Long> queryInfoBySpecIdAndEids(Long sellSpecificationsId, List<Long> eids);

    /**
     * 根据售卖规格ID和eid查询商品信息（只能保证唯一）
     *
     * @param sellSpecificationsIds
     * @param eid
     * @return
     */
    Map<Long, Long> queryInfoMapBySpecIdsAndEid(List<Long> sellSpecificationsIds, Long eid);

    /**
     * 批量修改商品状态
     *
     * @param request
     * @return
     */
    Boolean batchUpdateGoodsStatus(BatchUpdateGoodsStatusRequest request);

    /**
     * 批量修改商品状态
     *
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
     * 根据售卖规格ID列表和商业公司id查询商品信息
     * @param sellSpecificationsIdList
     * @param eids
     * @return
     */
    List<GoodsDTO> findGoodsBySellSpecificationsIdAndEid(List<Long> sellSpecificationsIdList,  List<Long> eids);

    /**
     * 通过以岭商品和配送商ID集合查询库存信息
     *
     * @param goodsId
     * @param distributorEids
     * @return key=配送商EID value=配送商商品Id
     */
    Map<Long, Long> getDistributorGoodsIdMapByYlGoodsId(List<Long> distributorEids,Long goodsId);

    /**
     * 通过以岭id和配送批量查询配送商品库存信息
     *
     * @param goodsIds
     * @param distributorEid
     * @return key=配送商商品ID value=配送商商品Id
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
     * 根据skuIds集合获取销售包装信息
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
     *商品Id集合获取以岭商品ID
     * @param goodsIds
     * @param yilingEids
     * @return
     */
    Map<Long, Long> getYilingGoodsIdByGoodsIdAndYilingEids(List<Long> goodsIds,List<Long> yilingEids);

    /**
     * 企业id和内码更新sku状态
     * @param eid
     * @param inSn
     * @param status
     * @return
     */
    Boolean updateSkuStatusByEidAndInSn(Long eid, String inSn, Integer status,Long updater);

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
     * 库存id获取订阅库存列表
     * @param
     * @return
     */
    List<InventorySubscriptionDTO> getInventoryDetailByInventoryId(Long inventoryId);


    /**
     * 根据商品ids获取产品线信息
     * @param goodsIdList
     * @return
     */
    List<GoodsLineBO> getGoodsLineByGoodsIds(List<Long> goodsIdList);

    /**
     * 更新保质期
     * @param request
     * @return
     */
    Boolean updateShelfLife(UpdateShelfLifeRequest request);
}
