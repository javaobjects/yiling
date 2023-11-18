package com.yiling.goods.medicine.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.bo.ChoicenessGoodsBO;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QuerySellSpecificationsGoodsIdBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.bo.SpecificationPriceBO;
import com.yiling.goods.medicine.dao.B2bGoodsMapper;
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
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.medicine.service.B2bGoodsService;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.entity.StandardGoodsDO;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;
import com.yiling.goods.standard.service.StandardGoodsPicService;
import com.yiling.goods.standard.service.StandardGoodsService;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * b2b商品表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-20
 */
@Service
public class B2bGoodsServiceImpl extends BaseServiceImpl<B2bGoodsMapper, B2bGoodsDO> implements B2bGoodsService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StandardGoodsService standardGoodsService;

    @Autowired
    private StandardGoodsPicService standardGoodsPicService;

    @Autowired
    private StandardGoodsSpecificationService standardGoodsSpecificationService;

    @Override
    public Map<Long, List<GoodsDTO>> getShopGoodsByEidAndLimit(List<Long> eidList, Integer limit) {
        Map<Long, List<GoodsDTO>> goodsMap = MapUtil.empty();
        if(CollectionUtil.isEmpty(eidList)){
            return goodsMap;
        }
        List<Long> allGid = ListUtil.toList();
        eidList.forEach(eid->{
            List<Long> gidList = this.baseMapper.getB2bGoodsSaleTopLimit(Arrays.asList(eid), limit);
            allGid.addAll(gidList);
        });
        List<GoodsDTO> goodsDTOList = goodsService.batchQueryInfo(allGid);
        if(CollectionUtil.isEmpty(goodsDTOList)){
            return goodsMap;
        }
        goodsMap = goodsDTOList.stream().collect(Collectors.groupingBy(GoodsDTO::getEid,Collectors.toList()));
        return goodsMap;
    }

    @Override
    public Page<GoodsListItemBO> queryB2bGoodsPageList(QueryGoodsPageListRequest request) {
        Page<GoodsListItemBO> page = this.baseMapper.queryB2bGoodsPageList(new Page<>(request.getCurrent(), request.getSize()), request);
        List<Long> standardIds = page.getRecords().stream().map(e -> e.getStandardId()).distinct().collect(Collectors.toList());

        //获取分类信息
        Map<Long, StandardGoodsDO> standardGoodsDOMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(standardIds) && standardIds.size() > 0) {
            List<StandardGoodsDO> standardGoodsDOList = standardGoodsService.listByIds(standardIds);
            standardGoodsDOMap = standardGoodsDOList.stream().collect(Collectors.toMap(StandardGoodsDO::getId, Function.identity()));
        }
        Map<Long, StandardGoodsDO> finalStandardGoodsDOMap = standardGoodsDOMap;

        //批量获取图片信息
        Map<Long, List<StandardGoodsPicDO>> standardGoodsPicMap = MapUtil.newHashMap();
        if (request.getIsShowDefaultPic() == null || request.getIsShowDefaultPic() == 0) {
            standardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);
        }
        Map<Long, List<StandardGoodsPicDO>> finalStandardGoodsPicMap = standardGoodsPicMap;

        page.getRecords().forEach(e -> {
            e.setPic(goodsService.getDefaultUrlByStandardGoodsPicList(finalStandardGoodsPicMap.get(e.getStandardId()), e.getSellSpecificationsId()));
            if (finalStandardGoodsDOMap.get(e.getStandardId()) != null) {
                e.setStandardCategoryName1(finalStandardGoodsDOMap.get(e.getStandardId()).getStandardCategoryName1());
                e.setStandardCategoryName2(finalStandardGoodsDOMap.get(e.getStandardId()).getStandardCategoryName2());
            }
        });
        return page;
    }

    @Override
    public List<QueryStatusCountBO> queryB2bStatusCountList(List<Long> eidList) {
        List<QueryStatusCountBO> list = this.baseMapper.queryB2bStatusCountList(eidList);
        return list;
    }

    @Override
    public List<QueryStatusCountBO> queryB2bStatusCountListByCondition(QueryGoodsPageListRequest request) {
        return this.baseMapper.queryB2bStatusCountListByCondition(request);
    }

    @Override
    public GoodsInfoDTO queryInfo(Long goodsId) {
        GoodsDTO goodsDTO = goodsService.queryInfo(goodsId);
        GoodsInfoDTO goodsInfoDTO = PojoUtils.map(goodsDTO, GoodsInfoDTO.class);
        B2bGoodsDTO b2bGoodsDTO = getB2bGoodsByGoodsId(goodsId);
        if (b2bGoodsDTO != null) {
            goodsInfoDTO.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());
            goodsInfoDTO.setOutReason(b2bGoodsDTO.getOutReason());
        }
        return goodsInfoDTO;
    }

    @Override
    public List<GoodsInfoDTO> batchQueryInfo(List<Long> goodsIds) {
        List<GoodsDTO> goodsDTOList = goodsService.batchQueryInfo(goodsIds);
        List<GoodsInfoDTO> goodsInfoDTOList = PojoUtils.map(goodsDTOList, GoodsInfoDTO.class);
        List<B2bGoodsDTO> b2bGoodsDTOList = getB2bGoodsListByGoodsIds(goodsIds);
        Map<Long, B2bGoodsDTO> b2bGoodsDTOMap = b2bGoodsDTOList.stream().collect(Collectors.toMap(B2bGoodsDTO::getGoodsId, Function.identity()));
        goodsInfoDTOList.forEach(e -> {
            B2bGoodsDTO b2bGoodsDTO = b2bGoodsDTOMap.get(e.getId());
            if (b2bGoodsDTO != null) {
                e.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());
                e.setOutReason(b2bGoodsDTO.getOutReason());
            }
        });
        return goodsInfoDTOList;
    }

    @Override
    public List<B2bGoodsDTO> getB2bGoodsListByGoodsIds(List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return ListUtil.empty();
        }
        QueryWrapper<B2bGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(B2bGoodsDO::getGoodsId, goodsIds);
        return PojoUtils.map(this.list(queryWrapper), B2bGoodsDTO.class);
    }

    @Override
    public B2bGoodsDTO getB2bGoodsByGoodsId(Long goodsId) {
        QueryWrapper<B2bGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(B2bGoodsDO::getGoodsId, goodsId);
        return PojoUtils.map(this.getOne(queryWrapper), B2bGoodsDTO.class);
    }

    @Override
    public Boolean updateB2bGoods(UpdateB2bGoodsRequest request) {
        QueryWrapper<B2bGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(B2bGoodsDO::getGoodsId, request.getGoodsId());

        B2bGoodsDO b2bGoodsDO = PojoUtils.map(request, B2bGoodsDO.class);
        return this.saveOrUpdate(b2bGoodsDO, queryWrapper);
    }

    /**
     * //判断如果没有设置价格和库存直接变成待编辑
     * if (!request.getGoodsStatus().equals(GoodsStatusEnum.WAIT_SET.getCode())) {
     * for (Long goodsId : request.getGoodsIds()) {
     * if (!goodsService.isWaitSetGoodsStatus(GoodsLineEnum.B2B.getCode(), goodsId)) {
     * throw new BusinessException(GoodsErrorCode.GOODS_NOT_SET);
     * }
     * }
     * }
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateGoodsStatus(BatchUpdateGoodsStatusRequest request) {
        //判断如果没有设置价格和库存直接变成待编辑
        List<Long> upAndDownGoodsIds = new ArrayList<>();
        List<Long> waitSetGoodsIds = new ArrayList<>();
        if (!request.getGoodsStatus().equals(GoodsStatusEnum.WAIT_SET.getCode())) {
            for (Long goodsId : request.getGoodsIds()) {
                if (!goodsService.isWaitSetGoodsStatus(GoodsLineEnum.B2B.getCode(), goodsId)) {
                    waitSetGoodsIds.add(goodsId);
                } else {
                    upAndDownGoodsIds.add(goodsId);
                }
            }
        }
        if (CollUtil.isNotEmpty(waitSetGoodsIds)) {
            QueryWrapper<B2bGoodsDO> waitGoodsWrapper = new QueryWrapper<>();
            waitGoodsWrapper.lambda().in(B2bGoodsDO::getGoodsId, waitSetGoodsIds);

            B2bGoodsDO b2bGoodsDO = new B2bGoodsDO();
            b2bGoodsDO.setOpUserId(request.getOpUserId());
            b2bGoodsDO.setGoodsStatus(GoodsStatusEnum.WAIT_SET.getCode());
            b2bGoodsDO.setOutReason(GoodsOutReasonEnum.DEFAULT.getCode());
            this.update(b2bGoodsDO, waitGoodsWrapper);
        }

        if (CollUtil.isNotEmpty(upAndDownGoodsIds)) {
            QueryWrapper<B2bGoodsDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(B2bGoodsDO::getGoodsId, request.getGoodsIds());
            List<B2bGoodsDO> list = this.list(queryWrapper);
            List<B2bGoodsDO> waitList = list.stream().filter(e -> e.getGoodsStatus().equals(GoodsStatusEnum.WAIT_SET.getCode())).collect(Collectors.toList());
            List<B2bGoodsDO> shelfList = list.stream().filter(e -> !e.getGoodsStatus().equals(GoodsStatusEnum.WAIT_SET.getCode())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(shelfList)) {
                QueryWrapper<B2bGoodsDO> shelfWrapper = new QueryWrapper<>();
                shelfWrapper.lambda().in(B2bGoodsDO::getGoodsId, shelfList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList()));
                //上架
                if (GoodsStatusEnum.UP_SHELF.getCode().equals(request.getGoodsStatus())) {
                    shelfWrapper.lambda().eq(B2bGoodsDO::getOutReason, request.getOutReason());

                    B2bGoodsDO b2bGoodsDO = new B2bGoodsDO();
                    b2bGoodsDO.setOpUserId(request.getOpUserId());
                    b2bGoodsDO.setGoodsStatus(request.getGoodsStatus());
                    b2bGoodsDO.setOutReason(GoodsOutReasonEnum.DEFAULT.getCode());
                    return this.update(b2bGoodsDO, shelfWrapper);
                } else {
                    B2bGoodsDO b2bGoodsDO = new B2bGoodsDO();
                    b2bGoodsDO.setOpUserId(request.getOpUserId());
                    b2bGoodsDO.setGoodsStatus(request.getGoodsStatus());
                    b2bGoodsDO.setOutReason(request.getOutReason());
                    return this.update(b2bGoodsDO, shelfWrapper);
                }
            }
            if (CollUtil.isNotEmpty(waitList)) {
                QueryWrapper<B2bGoodsDO> shelfWrapper = new QueryWrapper<>();
                shelfWrapper.lambda().in(B2bGoodsDO::getGoodsId, waitList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList()));
                B2bGoodsDO b2bGoodsDO = new B2bGoodsDO();
                b2bGoodsDO.setOpUserId(request.getOpUserId());
                b2bGoodsDO.setGoodsStatus(request.getGoodsStatus());
                //上架
                if (GoodsStatusEnum.UP_SHELF.getCode().equals(request.getGoodsStatus())) {
                    b2bGoodsDO.setOutReason(GoodsOutReasonEnum.DEFAULT.getCode());
                } else {
                    b2bGoodsDO.setOutReason(request.getOutReason());
                }
                return this.update(b2bGoodsDO, shelfWrapper);
            }
        }
        return true;
    }

    @Override
    public Boolean updateB2bLineStatus(BatchUpdateGoodsStatusRequest request) {
        if(CollectionUtil.isEmpty(request.getGoodsIds()) || null == request.getGoodsLineStatusEnum()){
            return true;
        }
        QueryWrapper<B2bGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(B2bGoodsDO::getGoodsId,request.getGoodsIds());
        B2bGoodsDO b2bGoodsDO = new B2bGoodsDO();
        b2bGoodsDO.setStatus(request.getGoodsLineStatusEnum().getCode());
        return this.update(b2bGoodsDO, wrapper);
    }

    @Override
    public List<GoodsDTO> getB2bGoodsSaleTopLimit(List<Long> eids, Integer limit) {
        List<Long> goodsIds = this.baseMapper.getB2bGoodsSaleTopLimit(eids, limit);
        if (CollUtil.isEmpty(goodsIds)) {
            return ListUtil.empty();
        }
        return goodsService.batchQueryInfo(goodsIds);
    }

    @Override
    public List<Long> getEidListBySaleGoods(List<Long> eids) {
        if (CollUtil.isEmpty(eids)) {
            return ListUtil.empty();
        }
        return this.baseMapper.getEidListBySaleGoods(eids);
    }

    @Override
    public Long countB2bGoodsByEids(List<Long> eids) {
        List<QueryStatusCountBO> list = this.queryB2bStatusCountList(eids);
        Long total = 0L;
        for (QueryStatusCountBO queryStatusCountBO : list) {
            if (queryStatusCountBO.getGoodsStatus().equals(GoodsStatusEnum.UP_SHELF.getCode())) {
                total = queryStatusCountBO.getCount();
            }
        }
        return total;
    }

    @Override
    public Map<Long, List<Long>> getSellerGoodsIdsBySellSpecificationsIds(List<Long> sellSpecificationsIds, List<Long> eids) {
        if (CollUtil.isEmpty(eids)||CollUtil.isEmpty(sellSpecificationsIds)) {
            return MapUtil.empty();
        }
        List<QuerySellSpecificationsGoodsIdBO> list = this.baseMapper.getSellerGoodsIdsBySellSpecificationsIds(sellSpecificationsIds, eids);
        Map<Long, List<Long>> specificationsMap = new HashMap<>();
        for (QuerySellSpecificationsGoodsIdBO querySellSpecificationsGoodsIdBO : list) {
            List<Long> specificationIdList = null;
            if (specificationsMap.containsKey(querySellSpecificationsGoodsIdBO.getSellSpecificationsId())) {
                specificationIdList = specificationsMap.get(querySellSpecificationsGoodsIdBO.getSellSpecificationsId());
                specificationIdList.add(querySellSpecificationsGoodsIdBO.getGoodsId());
            } else {
                specificationIdList = new ArrayList<>();
                specificationIdList.add(querySellSpecificationsGoodsIdBO.getGoodsId());
                specificationsMap.put(querySellSpecificationsGoodsIdBO.getSellSpecificationsId(), specificationIdList);
            }
        }
        return specificationsMap;
    }

    @Override
    public Map<Long, List<Long>> getSellerEidsBySellSpecificationsIds(List<Long> sellSpecificationsIds, List<Long> eids) {
        if (CollUtil.isEmpty(eids)||CollUtil.isEmpty(sellSpecificationsIds)) {
            return MapUtil.empty();
        }
        List<QuerySellSpecificationsGoodsIdBO> list = this.baseMapper.getSellerGoodsIdsBySellSpecificationsIds(sellSpecificationsIds, eids);
        Map<Long, List<Long>> map = list.stream().collect(Collectors.groupingBy(QuerySellSpecificationsGoodsIdBO::getSellSpecificationsId, Collectors.mapping(QuerySellSpecificationsGoodsIdBO::getEid, Collectors.toList())));
        return map;
    }

    @Override
    public List<Long> getIdsBySpecificationsIdsAndIncludeEidsAndCustomerEid(List<Long> sellSpecificationsIds,List<Long> includeEids,Long buyerEid) {
        return this.baseMapper.getIdsBySpecificationsIdsAndIncludeEidsAndCustomerEid(sellSpecificationsIds, includeEids, buyerEid);
    }

    @Override
    public Page<ChoicenessGoodsBO> getChoicenessByCustomerAndSellSpecificationsId(QueryChoicenessGoodsPageListRequest request) {
        return this.baseMapper.getChoicenessByCustomerAndSellSpecificationsId(new Page<>(request.getCurrent(), request.getSize()), request);
    }

    @Override
    public boolean updateB2bStatusByEid(UpdateGoodsStatusByEidRequest updateGoodsStatusByEidRequest) {
        List<Long> goodsIds = goodsService.getGoodsIdsByEid(updateGoodsStatusByEidRequest.getEid());
        List<B2bGoodsDTO> b2bGoodsDTOList = this.getB2bGoodsListByGoodsIds(goodsIds);
        List<Long> b2bGoodsIds = b2bGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(b2bGoodsIds)) {
            BatchUpdateGoodsStatusRequest request = new BatchUpdateGoodsStatusRequest();
            request.setGoodsLine(GoodsLineEnum.B2B.getCode());
            request.setGoodsIds(b2bGoodsIds);
            request.setGoodsStatus(GoodsStatusEnum.UN_SHELF.getCode());
            request.setOutReason(GoodsOutReasonEnum.REJECT.getCode());
            request.setOpUserId(updateGoodsStatusByEidRequest.getOpUserId());
            return this.updateGoodsStatus(request);
        }
        return true;
    }

    @Override
    public EnterpriseGoodsCountBO getGoodsCountByEid(Long eid) {
        return this.baseMapper.getGoodsCountByEid(eid);
    }

    @Override
    public Map<Long,EnterpriseGoodsCountBO> getGoodsCountByEidList(List<Long> eidList) {
        Map<Long,EnterpriseGoodsCountBO> countMap = MapUtil.empty();
        if(CollectionUtil.isEmpty(eidList)){
            return countMap;
        }
        List<EnterpriseGoodsCountBO> countList = this.baseMapper.getGoodsCountByEidList(eidList);
        if(CollectionUtil.isEmpty(countList)){
            return countMap;
        }
        countMap=countList.stream().collect(Collectors.toMap(EnterpriseGoodsCountBO::getEid, Function.identity()));
        return countMap;
    }

    @Override
    public List<GoodsListItemBO> getB2bGoodsBySellSpecificationsIdsAndEids(QueryGoodsPageListRequest request) {
        if(CollectionUtil.isNotEmpty(request.getEidList())&&CollectionUtil.isNotEmpty(request.getIncludeSellSpecificationsIds())){
            return this.baseMapper.queryB2bGoodsList(request);
        }
        return Lists.newArrayList();
    }

    @Override
    public Map<Long, BigDecimal> getMinPriceBySpecificationsIds(List<Long> sellSpecificationsIds) {
        if(CollectionUtil.isEmpty(sellSpecificationsIds)){
            return MapUtil.empty();
        }
        List<SpecificationPriceBO> minPriceList = this.baseMapper.getMinPriceBySpecificationsIds(sellSpecificationsIds);
        if(CollectionUtil.isEmpty(minPriceList)){
            return MapUtil.empty();
        }
        return minPriceList.stream().collect(Collectors.toMap(SpecificationPriceBO::getSellSpecificationId,SpecificationPriceBO::getPrice));
    }

    @Override
    public Page<GoodsListItemBO> queryWaterfallSpecificationPage(QuerySaleSpecificationPageListRequest request) {
        Page<Long> specIdPage = this.baseMapper.queryWaterfallSpecificationPage(request.getPage(), request);
        Page<GoodsListItemBO> page = new Page<>(specIdPage.getCurrent(),specIdPage.getSize());
        page.setTotal(specIdPage.getTotal());
        if(CollectionUtil.isNotEmpty(specIdPage.getRecords())){
            List<StandardGoodsSpecificationDTO> specificationList = standardGoodsSpecificationService.getListStandardGoodsSpecificationByIds(specIdPage.getRecords());
            if(CollectionUtil.isNotEmpty(specificationList)){
                Map<Long, StandardGoodsSpecificationDTO> specificationDTOMap = specificationList.stream().collect(Collectors.toMap(StandardGoodsSpecificationDTO::getId, Function.identity()));
                List<Long> standardIds = specificationList.stream().map(StandardGoodsSpecificationDTO::getStandardId).collect(Collectors.toList());
                Map<Long, List<StandardGoodsPicDO>> standardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);
                List<StandardGoodsDO> standardGoodsDOList = standardGoodsService.listByIds(standardIds);
                Map<Long, StandardGoodsDO>  standardGoodsMap = standardGoodsDOList.stream().collect(Collectors.toMap(StandardGoodsDO::getId, Function.identity()));

                LinkedList<GoodsListItemBO> list = ListUtil.toLinkedList();
                specIdPage.getRecords().forEach(specId->{
                    StandardGoodsSpecificationDTO specificationDTO = specificationDTOMap.get(specId);
                    if(null != specificationDTO){
                        GoodsListItemBO itemBO = PojoUtils.map(specificationDTO, GoodsListItemBO.class);
                        itemBO.setSellSpecificationsId(specificationDTO.getId());
                        StandardGoodsDO standardGoods = standardGoodsMap.get(specificationDTO.getStandardId());
                        itemBO.setGdfName(null == standardGoods? "":standardGoods.getGdfName());
                        itemBO.setPic(goodsService.getDefaultUrlByStandardGoodsPicList(standardGoodsPicMap.get(specificationDTO.getStandardId()), specificationDTO.getId()));
                        list.add(itemBO);
                    }
                });
                page.setRecords(list);
            }
        }
        return page;
    }

    @Override
    public List<Long> getInStockGoodsBySpecId(Long sellSpecificationsId) {
        Assert.notNull(sellSpecificationsId,"标准商品规格不能为空");
        return this.baseMapper.getInStockGoodsBySpecId(sellSpecificationsId);
    }

    @Override
    public Page<ChoicenessGoodsBO> queryDistributorGoodsBySpec(QueryChoicenessGoodsPageListRequest request) {
        Assert.notNull(request.getSellSpecificationsId(),"标准商品规格不能为空");
        if(request.getFilterPurchaseEnterprise() && CollectionUtil.isEmpty(request.getPurchaseEids())){
            return new Page<>(request.getCurrent(),request.getSize());
        }
        if(request.getFilterInStockGoods() && CollectionUtil.isEmpty(request.getInStockGoodsIds())){
            return new Page<>(request.getCurrent(),request.getSize());
        }
        if(request.getFilterProvince() && StringUtils.isBlank(request.getProvinceCode())){
            return new Page<>(request.getCurrent(),request.getSize());
        }
        return this.baseMapper.queryDistributorGoodsBySpec(request.getPage(),request);
    }
}
