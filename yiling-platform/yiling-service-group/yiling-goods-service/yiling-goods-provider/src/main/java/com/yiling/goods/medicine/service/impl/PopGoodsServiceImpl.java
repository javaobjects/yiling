package com.yiling.goods.medicine.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dao.PopGoodsMapper;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsStatusRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.UpdatePopGoodsRequest;
import com.yiling.goods.medicine.entity.B2bGoodsDO;
import com.yiling.goods.medicine.entity.GoodsDO;
import com.yiling.goods.medicine.entity.PopGoodsDO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.medicine.service.PopGoodsService;
import com.yiling.goods.standard.entity.StandardGoodsDO;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;
import com.yiling.goods.standard.service.StandardGoodsPicService;
import com.yiling.goods.standard.service.StandardGoodsService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * pop商品表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-20
 */
@Service
public class PopGoodsServiceImpl extends BaseServiceImpl<PopGoodsMapper, PopGoodsDO> implements PopGoodsService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StandardGoodsService standardGoodsService;

    @Autowired
    private StandardGoodsPicService standardGoodsPicService;

    @Override
    public Page<GoodsListItemBO> queryPopGoodsPageList(QueryGoodsPageListRequest request) {
        Page<GoodsListItemBO> page = this.baseMapper.queryPopGoodsPageList(new Page<>(request.getCurrent(), request.getSize()), request);
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
    public List<GoodsListItemBO> queryPopGoodsList(QueryGoodsPageListRequest request) {
        List<GoodsListItemBO> goodsList = this.baseMapper.queryPopGoodsList(request);
        return goodsList;
    }

    @Override
    public List<QueryStatusCountBO> queryPopStatusCountList(List<Long> eidList) {
        List<QueryStatusCountBO> list = this.baseMapper.queryPopStatusCountList(eidList);
        return list;
    }

    @Override
    public List<QueryStatusCountBO> queryPopStatusCountListByCondition(QueryGoodsPageListRequest request) {
        return this.baseMapper.queryPopStatusCountListByCondition(request);
    }

    @Override
    public GoodsInfoDTO findGoodsByInSnAndEid(Long eid, String inSn) {
        List<GoodsDO> list = goodsService.getGoodsListByEid(eid);
        Map<Long,GoodsDO> goodsDOMap=list.stream().collect(Collectors.toMap(GoodsDO::getId,Function.identity()));
        List<Long> goodsIds = list.stream().map(e -> e.getId()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(goodsIds)) {
            List<GoodsSkuDTO> goodsSkuDTOList = goodsService.getGoodsSkuByGoodsIds(goodsIds);
            goodsSkuDTOList = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
            for (GoodsSkuDTO goodsSkuDTO : goodsSkuDTOList) {
                if (goodsSkuDTO.getInSn().equals(inSn)) {
                    PopGoodsDTO popGoodsDTO = this.getPopGoodsByGoodsId(goodsSkuDTO.getGoodsId());
                    GoodsInfoDTO goodsInfoDTO = PojoUtils.map(goodsDOMap.get(goodsSkuDTO.getGoodsId()), GoodsInfoDTO.class);
                    goodsInfoDTO.setOutReason(popGoodsDTO.getOutReason());
                    goodsInfoDTO.setGoodsStatus(popGoodsDTO.getGoodsStatus());
                    goodsInfoDTO.setIsPatent(popGoodsDTO.getIsPatent());
                    return goodsInfoDTO;
                }
            }
        }
        return null;
    }

    @Override
    public GoodsInfoDTO queryInfo(Long goodsId) {
        GoodsDTO goodsDTO = goodsService.queryInfo(goodsId);
        if(Objects.isNull(goodsDTO)){
            return null;
        }
        GoodsInfoDTO goodsInfoDTO = PojoUtils.map(goodsDTO, GoodsInfoDTO.class);
        PopGoodsDTO popGoodsDTO = getPopGoodsByGoodsId(goodsId);
        if(null!=popGoodsDTO){
            goodsInfoDTO.setGoodsStatus(popGoodsDTO.getGoodsStatus());
            goodsInfoDTO.setOutReason(popGoodsDTO.getOutReason());
        }
        return goodsInfoDTO;
    }

    @Override
    public List<GoodsInfoDTO> batchQueryInfo(List<Long> goodsIds) {
        List<GoodsDTO> goodsDTOList = goodsService.batchQueryInfo(goodsIds);
        List<GoodsInfoDTO> goodsInfoDTOList = PojoUtils.map(goodsDTOList, GoodsInfoDTO.class);
        List<PopGoodsDTO> popGoodsDTOList = getPopGoodsListByGoodsIds(goodsIds);
        Map<Long, PopGoodsDTO> popGoodsDTOMap = popGoodsDTOList.stream().collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity()));
        goodsInfoDTOList.forEach(e -> {
            PopGoodsDTO popGoodsDTO = popGoodsDTOMap.get(e.getId());
            if(popGoodsDTO!=null) {
                e.setGoodsStatus(popGoodsDTO.getGoodsStatus());
                e.setOutReason(popGoodsDTO.getOutReason());
            }
        });
        return goodsInfoDTOList;
    }

    @Override
    public List<GoodsInfoDTO> batchQueryPopGoods(List<Long> goodsIds) {
        List<PopGoodsDTO> popGoodsList = this.getPopGoodsListByGoodsIds(goodsIds);
        List<Long> popGoodsIds = popGoodsList.stream().map(PopGoodsDTO::getGoodsId).collect(Collectors.toList());
        List<GoodsDTO> goodsList = goodsService.batchQueryInfo(popGoodsIds);
        List<GoodsInfoDTO> goodsInfoList = PojoUtils.map(goodsList, GoodsInfoDTO.class);
        Map<Long, PopGoodsDTO> popGoodsMap = popGoodsList.stream().collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity()));
        goodsInfoList.forEach(e->{
            PopGoodsDTO popGoodsDTO = popGoodsMap.get(e.getId());
            e.setGoodsStatus(popGoodsDTO.getGoodsStatus());
            e.setOutReason(popGoodsDTO.getOutReason());
        });
        return goodsInfoList;
    }

    @Override
    public PopGoodsDTO getPopGoodsByGoodsId(Long goodsId) {
        QueryWrapper<PopGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PopGoodsDO::getGoodsId, goodsId);
        return PojoUtils.map(this.getOne(queryWrapper), PopGoodsDTO.class);
    }

    @Override
    public List<PopGoodsDTO> getPopGoodsListByGoodsIds(List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return ListUtil.empty();
        }

        QueryWrapper<PopGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(PopGoodsDO::getGoodsId, goodsIds);
        return PojoUtils.map(this.list(queryWrapper), PopGoodsDTO.class);
    }

    @Override
    public Boolean updatePopGoods(UpdatePopGoodsRequest request) {
        QueryWrapper<PopGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PopGoodsDO::getGoodsId, request.getGoodsId());

        PopGoodsDO popGoodsDO = PojoUtils.map(request, PopGoodsDO.class);
        return this.saveOrUpdate(popGoodsDO, queryWrapper);
    }

    @Override
    public Boolean updateGoodsStatus(BatchUpdateGoodsStatusRequest request) {
        //判断如果没有设置价格和库存直接变成待编辑
        List<Long> upAndDownGoodsIds = new ArrayList<>();
        List<Long> waitSetGoodsIds = new ArrayList<>();
        if (!request.getGoodsStatus().equals(GoodsStatusEnum.WAIT_SET.getCode())) {
            for (Long goodsId : request.getGoodsIds()) {
                if (!goodsService.isWaitSetGoodsStatus(GoodsLineEnum.POP.getCode(), goodsId)) {
                    waitSetGoodsIds.add(goodsId);
                } else {
                    upAndDownGoodsIds.add(goodsId);
                }
            }
        }
        if (CollUtil.isNotEmpty(waitSetGoodsIds)) {
            QueryWrapper<PopGoodsDO> waitGoodsWrapper = new QueryWrapper<>();
            waitGoodsWrapper.lambda().in(PopGoodsDO::getGoodsId, waitSetGoodsIds);

            PopGoodsDO popGoodsDO = new PopGoodsDO();
            popGoodsDO.setOpUserId(request.getOpUserId());
            popGoodsDO.setGoodsStatus(GoodsStatusEnum.WAIT_SET.getCode());
            popGoodsDO.setOutReason(GoodsOutReasonEnum.DEFAULT.getCode());
            this.update(popGoodsDO, waitGoodsWrapper);
        }

        if (CollUtil.isNotEmpty(upAndDownGoodsIds)) {
            QueryWrapper<PopGoodsDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(PopGoodsDO::getGoodsId, request.getGoodsIds());
            List<PopGoodsDO> list = this.list(queryWrapper);
            List<PopGoodsDO> waitList = list.stream().filter(e -> e.getGoodsStatus().equals(GoodsStatusEnum.WAIT_SET.getCode())).collect(Collectors.toList());
            List<PopGoodsDO> shelfList = list.stream().filter(e -> !e.getGoodsStatus().equals(GoodsStatusEnum.WAIT_SET.getCode())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(shelfList)) {
                QueryWrapper<PopGoodsDO> shelfWrapper = new QueryWrapper<>();
                shelfWrapper.lambda().in(PopGoodsDO::getGoodsId, shelfList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList()));
                //上架
                if (GoodsStatusEnum.UP_SHELF.getCode().equals(request.getGoodsStatus())) {
                    shelfWrapper.lambda().eq(PopGoodsDO::getOutReason, request.getOutReason());

                    PopGoodsDO popGoodsDO = new PopGoodsDO();
                    popGoodsDO.setOpUserId(request.getOpUserId());
                    popGoodsDO.setGoodsStatus(request.getGoodsStatus());
                    popGoodsDO.setOutReason(GoodsOutReasonEnum.DEFAULT.getCode());
                    return this.update(popGoodsDO, shelfWrapper);
                } else {
                    PopGoodsDO popGoodsDO = new PopGoodsDO();
                    popGoodsDO.setOpUserId(request.getOpUserId());
                    popGoodsDO.setGoodsStatus(request.getGoodsStatus());
                    popGoodsDO.setOutReason(request.getOutReason());
                    return this.update(popGoodsDO, shelfWrapper);
                }
            }
            if (CollUtil.isNotEmpty(waitList)) {
                QueryWrapper<PopGoodsDO> shelfWrapper = new QueryWrapper<>();
                shelfWrapper.lambda().in(PopGoodsDO::getGoodsId, waitList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList()));
                PopGoodsDO popGoodsDO = new PopGoodsDO();
                popGoodsDO.setOpUserId(request.getOpUserId());
                popGoodsDO.setGoodsStatus(request.getGoodsStatus());
                //上架
                if (GoodsStatusEnum.UP_SHELF.getCode().equals(request.getGoodsStatus())) {
                    popGoodsDO.setOutReason(GoodsOutReasonEnum.DEFAULT.getCode());
                } else {
                    popGoodsDO.setOutReason(request.getOutReason());
                }
                return this.update(popGoodsDO, shelfWrapper);
            }
        }
        return true;
    }

    @Override
    public Boolean updatePopLineStatus(BatchUpdateGoodsStatusRequest request) {
        if(CollectionUtil.isEmpty(request.getGoodsIds()) || null == request.getGoodsLineStatusEnum()){
            return true;
        }
        QueryWrapper<PopGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PopGoodsDO::getGoodsId,request.getGoodsIds());
        PopGoodsDO popGoodsDO = new PopGoodsDO();
        popGoodsDO.setStatus(request.getGoodsLineStatusEnum().getCode());
        return this.update(popGoodsDO, wrapper);
    }

    @Override
    public EnterpriseGoodsCountBO getGoodsCountByEid(Long eid) {
        Long standardCount = this.baseMapper.getStandardCountByEid(eid);
        Long sellSpecificationCount = this.baseMapper.getSellSpecificationCountByEid(eid);
        EnterpriseGoodsCountBO enterpriseGoodsCountBO = new EnterpriseGoodsCountBO();
        enterpriseGoodsCountBO.setStandardCount(standardCount);
        enterpriseGoodsCountBO.setSellSpecificationCount(sellSpecificationCount);
        return enterpriseGoodsCountBO;
    }

    @Override
    public List<GoodsListItemBO> findGoodsBySpecificationIdAndEids(Long specificationId, List<Long> eids) {
        List<GoodsListItemBO> itemBOList = this.baseMapper.queryPopGoodsBySpecificationIdAndEid(specificationId, eids);
        return itemBOList;
    }

    @Override
    public List<GoodsListItemBO> findGoodsBySpecificationIdListAndEidList(List<Long> specificationIdList, List<Long> eidList, GoodsStatusEnum goodsStatusEnum) {
        if(CollectionUtil.isEmpty(specificationIdList)||CollectionUtil.isEmpty(eidList)){
            return ListUtil.empty();
        }
        if(null!=goodsStatusEnum){
            return this.baseMapper.findGoodsBySpecificationIdListAndEidList(specificationIdList, eidList,goodsStatusEnum.getCode());
        }
        return this.baseMapper.findGoodsBySpecificationIdListAndEidList(specificationIdList, eidList,null);
    }

    @Override
    public List<PopGoodsDTO> queryPopGoods(QueryGoodsPageListRequest request) {
        return PojoUtils.map(this.baseMapper.queryPopGoods(request),PopGoodsDTO.class);
    }

    @Override
    public Page<PopGoodsDTO> queryPopGoodsPage(QueryGoodsPageListRequest request) {
        return PojoUtils.map(this.baseMapper.queryPopGoods(request.getPage(),request),PopGoodsDTO.class);
    }
}
