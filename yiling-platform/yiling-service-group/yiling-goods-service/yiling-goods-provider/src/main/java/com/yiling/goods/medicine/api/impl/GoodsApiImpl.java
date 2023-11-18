package com.yiling.goods.medicine.api.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.medicine.api.GoodsApi;
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
import com.yiling.goods.medicine.dto.request.UpdateShelfLifeRequest;
import com.yiling.goods.medicine.service.GoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/5/20
 */
@DubboService
@Slf4j
public class GoodsApiImpl implements GoodsApi {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public Long saveGoods(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest) {
        Long gid = goodsService.saveGoods(saveOrUpdateGoodsRequest);
        return gid;
    }

    @Override
    public Long editGoods(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest) {
        Long gid = goodsService.editGoods(saveOrUpdateGoodsRequest);
        //刷新库存
        inventoryService.sendInventoryMessage(Arrays.asList(gid));
        return gid;
    }

    @Override
    public Long saveGoodsByErp(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest) {
        Long gid = goodsService.saveGoodsByErp(saveOrUpdateGoodsRequest);
        //刷新库存
        inventoryService.sendInventoryMessage(Arrays.asList(gid));
        return gid;
    }

    @Override
    public Boolean updateGoodsLine(UpdateGoodsLineRequest request) {
        return goodsService.updateGoodsLine(request);
    }

    @Override
    public Boolean updateAuditStatusByGoodsId(UpdateAuditStatusByGoodsIdRequest request) {
        return goodsService.updateAuditStatusByGoodsId(request);
    }

    @Override
    public MatchGoodsBO matchSellSpecificationsByGoods(MatchGoodsRequest request) {
        return goodsService.matchSellSpecificationsByGoods(request);
    }

    @Override
    public Page<GoodsListItemBO> queryPageListGoods(QueryGoodsPageListRequest request) {
        return goodsService.queryPageListGoods(request);
    }

    @Override
    public List<GoodsDTO> batchQueryInfo(List<Long> goodsIds) {
        return goodsService.batchQueryInfo(goodsIds);
    }

    @Override
    public List<GoodsSkuInfoDTO> batchQueryInfoBySkuIds(List<Long> skuIds) {
        return goodsService.batchQueryInfoBySkuIds(skuIds);
    }

    @Override
    public List<Long> getGoodsIdsByEid(Long eid) {
        return goodsService.getGoodsIdsByEid(eid);
    }

    @Override
    public List<QueryStatusCountBO> getCountByEid(Long eid) {
        return goodsService.getCountByEid(eid);
    }

    @Override
    public List<GoodsDTO> getGoodsListByEid(Long eid) {
        return PojoUtils.map(goodsService.getGoodsListByEid(eid), GoodsDTO.class);
    }

    @Override
    public GoodsDTO findGoodsAuditPassByInSnAndEidAndGoodsLine(Long eid, String inSn, Integer goodsLine) {
        return goodsService.findGoodsAuditPassByInSnAndEidAndGoodsLine(eid, inSn, goodsLine);
    }

    @Override
    public GoodsDTO findGoodsByInSnAndEidAndGoodsLine(Long eid, String inSn, Integer goodsLine) {
        return goodsService.findGoodsByInSnAndEidAndGoodsLine(eid, inSn, goodsLine);
    }

    @Override
    public GoodsDTO queryInfo(Long goodsId) {
        return goodsService.queryInfo(goodsId);
    }

    @Override
    public GoodsSkuInfoDTO getGoodsSkuInfoById(Long skuId) {
        return goodsService.getGoodsSkuInfoById(skuId);
    }

    @Override
    public GoodsFullDTO queryFullInfo(Long goodsId) {
        return goodsService.queryFullInfo(goodsId);
    }

    @Override
    public GoodsSkuFullDTO queryFullInfoBySkuId(Long skuId) {
        return goodsService.queryFullInfoBySkuId(skuId);
    }

    @Override
    public Map<Long, String> getPictureUrlMapByGoodsIds(List<Long> goodsIds) {
        return goodsService.getPictureUrlMapByGoodsIds(goodsIds);
    }

    @Override
    public List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasic(List<Long> goodsIds) {
        return goodsService.batchQueryStandardGoodsBasic(goodsIds);
    }

    @Override
    public List<GoodsSkuStandardBasicDTO> batchQueryStandardGoodsBasicBySkuIds(List<Long> skuIds) {
        return goodsService.batchQueryStandardGoodsBasicBySkuIds(skuIds);
    }

    @Override
    public Map<Long, List<GoodsDTO>> batchQueryDistributorGoodsInfo(List<QueryDistributorGoodsRequest> requestList) {
        return goodsService.batchQueryDistributorGoodsInfo(requestList);
    }

    @Override
    public Map<Long, Long> getDistributorGoodsIdMapByYlGoodsId(List<Long> distributorEids, Long goodsId) {
        return goodsService.getDistributorGoodsIdMapByYlGoodsId(distributorEids, goodsId);
    }

    @Override
    public Map<Long, Long> getDistributorGoodsMapByDistributorEid(List<Long> goodsIds, Long distributorEid) {
        return goodsService.getDistributorGoodsMapByDistributorEid(goodsIds, distributorEid);
    }

    @Override
    public Page<DistributorAgreementGoodsBO> getDistributorGoodsByrAgreementGoodsPageList(Map<Long, String> goodsAgreementMap, Long distributorEid, int current, int size) {
        return goodsService.getDistributorGoodsByrAgreementGoodsPageList(goodsAgreementMap,distributorEid,current,size);
    }

    @Override
    public Boolean batchUpdateGoodsStatus(BatchUpdateGoodsStatusRequest request) {
        return goodsService.batchUpdateGoodsStatus(request);
    }

    @Override
    public Boolean batchUpdateGoodsOverSold(BatchUpdateGoodsOverSoldRequest request) {
        return goodsService.batchUpdateGoodsOverSold(request);
    }

    @Override
    public Page<GoodsLogPageListItemDTO> queryGoodsLogPageList(QueryGoodsLogRequest request) {
        return goodsService.queryGoodsLogPageList(request);
    }

    @Override
    public List<GoodsDTO> findGoodsBySellSpecificationsIdAndEid(List<Long> sellSpecificationsIdList, List<Long> eid) {
        return goodsService.findGoodsBySellSpecificationsIdAndEid(sellSpecificationsIdList, eid);
    }

    @Override
    public List<GoodsLineBO> getGoodsLineByGoodsIds(List<Long> goodsIdList) {
        return goodsService.getGoodsLineByGoodsIds(goodsIdList);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsId(Long goodsId) {
        return goodsService.getGoodsSkuByGoodsId(goodsId);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIdAndStatus(Long goodsId, List<Integer> statusList) {
        return goodsService.getGoodsSkuByGoodsIdAndStatus(goodsId,statusList);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIds(List<Long> goodsIds) {
        return goodsService.getGoodsSkuByGoodsIds(goodsIds);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIdsAndStatus(List<Long> goodsIds, List<Integer> statusList) {
        return goodsService.getGoodsSkuByGoodsIdsAndStatus(goodsIds,statusList);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByIds(List<Long> skuIds) {
        return goodsService.getGoodsSkuByIds(skuIds);
    }

    @Override
    public Boolean isWaitSetGoodsStatus(Integer goodsLine, Long goodsId) {
        return goodsService.isWaitSetGoodsStatus(goodsLine, goodsId);
    }

    @Override
    public Long queryInfoBySpecIdAndEid(Long specId, Long eid) {
        return goodsService.queryInfoBySpecIdAndEid(specId, eid);
    }

    @Override
    public Map<Long, Long> getYilingGoodsIdByGoodsIdAndYilingEids(List<Long> goodsIds,List<Long> yilingEids) {
        return goodsService.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIds,yilingEids);
    }

    @Override
    public Boolean updateSkuStatusByEidAndInSn(Long eid, String inSn, Integer status,Long updater) {
        return goodsService.updateSkuStatusByEidAndInSn(eid,inSn,status,updater);
    }

    @Override
    public String goodsMerge(MergeGoodsRequest mergeGoodsRequest) {
        return goodsService.goodsMerge(mergeGoodsRequest);
    }

    @Override
    public GoodsDTO getYlGoodsByEidAndInSn(Long eid, String inSn, List<Long> yilingEids) {
        return goodsService.getYlGoodsByEidAndInSn(eid,inSn,yilingEids);
    }

    @Override
    public GoodsSkuDTO getGoodsSkuById(Long skuId) {
        return goodsService.getGoodsSkuById(skuId);
    }

    @Override
    public List<InventorySubscriptionDTO> getInventoryDetailByInventoryId(Long inventoryId) {
        return goodsService.getInventoryDetailByInventoryId(inventoryId);
    }

    @Override
    public Boolean updateShelfLife(UpdateShelfLifeRequest request) {
        return goodsService.updateShelfLife(request);
    }

}
