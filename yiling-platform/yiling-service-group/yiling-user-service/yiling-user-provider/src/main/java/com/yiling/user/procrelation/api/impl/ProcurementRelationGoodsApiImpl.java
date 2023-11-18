package com.yiling.user.procrelation.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;
import com.yiling.user.procrelation.dto.ProcRelationRebateResultBO;
import com.yiling.user.procrelation.dto.ProcurementRelationGoodsDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsForProcRelationRequest;
import com.yiling.user.procrelation.dto.request.CalculateProcRelationRebateRequest;
import com.yiling.user.procrelation.dto.request.QueryGoodsByEachOtherRequest;
import com.yiling.user.procrelation.dto.request.QueryRelationGoodsPageRequest;
import com.yiling.user.procrelation.dto.request.QuerySpecByBuyerPageRequest;
import com.yiling.user.procrelation.entity.ProcurementRelationDO;
import com.yiling.user.procrelation.entity.ProcurementRelationGoodsDO;
import com.yiling.user.procrelation.service.ProcurementRelationGoodsService;
import com.yiling.user.procrelation.service.ProcurementRelationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2023-05-22
 */
@Slf4j
@DubboService
public class ProcurementRelationGoodsApiImpl implements ProcurementRelationGoodsApi {

    @Autowired
    ProcurementRelationGoodsService goodsService;
    @Autowired
    ProcurementRelationService relationService;

    @Override
    public Boolean saveGoodsForProcRelation(List<AddGoodsForProcRelationRequest> requestList) {
        return goodsService.addGoodsForProcRelation(requestList);
    }

    @Override
    public Page<Long> queryGoodsPageByBuyer(QuerySpecByBuyerPageRequest request) {
        return goodsService.queryGoodsPageByBuyer(request);
    }

    @Override
    public Map<Long, List<DistributorGoodsBO>> getDistributorBySpecIdAndBuyer(List<Long> specIds, Long buyerEid) {
        Map<Long, List<DistributorGoodsBO>> result = MapUtil.newHashMap();
        if (CollUtil.isEmpty(specIds) || ObjectUtil.isNull(buyerEid) || ObjectUtil.equal(0L, buyerEid)) {
            return result;
        }
        //查询采购关系
        List<ProcurementRelationDO> relationList = relationService.queryInProRelationListByChannelEid(buyerEid);
        if (CollUtil.isEmpty(relationList)) {
            return result;
        }
        //查询关系下的品
        LambdaQueryWrapper<ProcurementRelationGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ProcurementRelationGoodsDO::getRelationId, relationList.stream().map(ProcurementRelationDO::getId).collect(Collectors.toList()));
        wrapper.in(ProcurementRelationGoodsDO::getSellSpecificationsId, specIds);
        List<ProcurementRelationGoodsDO> relationGoodsList = goodsService.list(wrapper);
        if (CollUtil.isEmpty(relationGoodsList)) {
            return result;
        }
        Map<Long, ProcurementRelationDO> relationMap = relationList.stream().collect(Collectors.toMap(ProcurementRelationDO::getId, e -> e));

        //根据采购关系下的品组织数据
        relationGoodsList.forEach(e -> {
            Long specId = e.getSellSpecificationsId();
            //找到采购关系
            ProcurementRelationDO relation = relationMap.get(e.getRelationId());
            //从采购关系找出配送商id
            Long deliveryEid = relation.getDeliveryEid();
            if (ObjectUtil.isNull(deliveryEid) || ObjectUtil.equal(0L, deliveryEid)) {
                log.error("采购关系没有配送商，采购关系id={}", relation.getId());
                return;
            }

            DistributorGoodsBO var = PojoUtils.map(e, DistributorGoodsBO.class);
            var.setDistributorEid(deliveryEid);

            if (result.containsKey(specId)) {
                List<DistributorGoodsBO> deliveryEidList = result.get(specId);
                deliveryEidList.add(var);
                List<DistributorGoodsBO> newDeliveryIdList = deliveryEidList.stream().distinct().collect(Collectors.toList());
                result.put(specId, newDeliveryIdList);
            } else {
                result.put(specId, ListUtil.toList(var));
            }
        });
        return result;
    }

    @Override
    public Map<Long, List<DistributorGoodsBO>> getDistributorByYlGoodsIdAndBuyer(List<Long> ylGoodsIds, Long buyerEid) {
        Map<Long, List<DistributorGoodsBO>> result = MapUtil.newHashMap();
        if (CollUtil.isEmpty(ylGoodsIds) || ObjectUtil.isNull(buyerEid) || ObjectUtil.equal(0L, buyerEid)) {
            return result;
        }
        //查询采购关系
        List<ProcurementRelationDO> relationList = relationService.queryInProRelationListByChannelEid(buyerEid);
        if (CollUtil.isEmpty(relationList)) {
            return result;
        }
        //查询关系下的品
        LambdaQueryWrapper<ProcurementRelationGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ProcurementRelationGoodsDO::getRelationId, relationList.stream().map(ProcurementRelationDO::getId).collect(Collectors.toList()));
        wrapper.in(ProcurementRelationGoodsDO::getGoodsId, ylGoodsIds);
        List<ProcurementRelationGoodsDO> relationGoodsList = goodsService.list(wrapper);
        if (CollUtil.isEmpty(relationGoodsList)) {
            return result;
        }
        Map<Long, ProcurementRelationDO> relationMap = relationList.stream().collect(Collectors.toMap(ProcurementRelationDO::getId, e -> e));

        //根据采购关系下的品组织数据
        relationGoodsList.forEach(e -> {
            Long goodsId = e.getGoodsId();
            //找到采购关系
            ProcurementRelationDO relation = relationMap.get(e.getRelationId());
            //从采购关系找出配送商id
            Long deliveryEid = relation.getDeliveryEid();
            if (ObjectUtil.isNull(deliveryEid) || ObjectUtil.equal(0L, deliveryEid)) {
                log.error("采购关系没有配送商，采购关系id={}", relation.getId());
                return;
            }
            DistributorGoodsBO var = PojoUtils.map(e, DistributorGoodsBO.class);
            var.setDistributorEid(deliveryEid);

            if (result.containsKey(goodsId)) {
                List<DistributorGoodsBO> deliveryEidList = result.get(goodsId);
                deliveryEidList.add(var);
                List<DistributorGoodsBO> newDeliveryIdList = deliveryEidList.stream().distinct().collect(Collectors.toList());
                result.put(goodsId, newDeliveryIdList);
            } else {
                result.put(goodsId, ListUtil.toList(var));
            }
        });
        return result;
    }

    @Override
    public List<Long> getSpecBySpecIds(List<Long> specIds, Long buyerEid) {
        List<Long> result = ListUtil.toList();
        if (CollUtil.isEmpty(specIds) || ObjectUtil.isNull(buyerEid) || ObjectUtil.equal(0L, buyerEid)) {
            return result;
        }
        //查询采购关系
        List<ProcurementRelationDO> relationList = relationService.queryInProRelationListByChannelEid(buyerEid);
        if (CollUtil.isEmpty(relationList)) {
            return result;
        }
        //查询关系下的品
        LambdaQueryWrapper<ProcurementRelationGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ProcurementRelationGoodsDO::getRelationId, relationList.stream().map(ProcurementRelationDO::getId).collect(Collectors.toList()));
        wrapper.in(ProcurementRelationGoodsDO::getSellSpecificationsId, specIds);
        List<ProcurementRelationGoodsDO> relationGoodsList = goodsService.list(wrapper);
        if (CollUtil.isEmpty(relationGoodsList)) {
            return result;
        }
        return relationGoodsList.stream().map(ProcurementRelationGoodsDO::getSellSpecificationsId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<Long> getYlGoodsIdByYlGoodsIds(List<Long> ylGoodsId, Long buyerEid) {
        List<Long> result = ListUtil.toList();
        if (CollUtil.isEmpty(ylGoodsId) || ObjectUtil.isNull(buyerEid) || ObjectUtil.equal(0L, buyerEid)) {
            return result;
        }
        //查询采购关系
        List<ProcurementRelationDO> relationList = relationService.queryInProRelationListByChannelEid(buyerEid);
        if (CollUtil.isEmpty(relationList)) {
            return result;
        }
        //查询关系下的品
        LambdaQueryWrapper<ProcurementRelationGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ProcurementRelationGoodsDO::getRelationId, relationList.stream().map(ProcurementRelationDO::getId).collect(Collectors.toList()));
        wrapper.in(ProcurementRelationGoodsDO::getGoodsId, ylGoodsId);
        List<ProcurementRelationGoodsDO> relationGoodsList = goodsService.list(wrapper);
        if (CollUtil.isEmpty(relationGoodsList)) {
            return result;
        }
        return relationGoodsList.stream().map(ProcurementRelationGoodsDO::getGoodsId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<ProcurementRelationGoodsDTO> queryGoodsByRelationId(Long relationId) {
        List<ProcurementRelationGoodsDO> list = goodsService.queryGoodsByRelationId(relationId);
        return PojoUtils.map(list, ProcurementRelationGoodsDTO.class);
    }

    @Override
    public Page<ProcurementRelationGoodsDTO> queryProcRelationGoodsPage(QueryRelationGoodsPageRequest request) {
        Page<ProcurementRelationGoodsDO> page = goodsService.queryProcRelationGoodsPage(request);
        return PojoUtils.map(page, ProcurementRelationGoodsDTO.class);
    }

    @Override
    public List<DistributorGoodsBO> queryGoodsListByEachOther(QueryGoodsByEachOtherRequest request) {
        List<ProcurementRelationDO> relations = relationService.queryInProRelationListByEachOther(request.getSellerEid(), request.getBuyerEid());
        if (ObjectUtil.isNull(relations)) {
            return ListUtil.empty();
        }
        List<ProcurementRelationGoodsDO> goodsDOList = goodsService.queryGoodsByRelationIdAndGoodsName(relations.stream().map(ProcurementRelationDO::getId).collect(Collectors.toList()), request.getGoodsName());
        List<DistributorGoodsBO> list = PojoUtils.map(goodsDOList, DistributorGoodsBO.class);
        Map<Long, ProcurementRelationDO> relationDOMap = relations.stream().collect(Collectors.toMap(ProcurementRelationDO::getId, e -> e));
        list.forEach(e -> {
            e.setDistributorEid(relationDOMap.get(e.getRelationId()).getDeliveryEid());
        });
        return list;
    }

    @Override
    public List<ProcRelationRebateResultBO> calculateRebateForGoods(CalculateProcRelationRebateRequest request) {
        List<ProcRelationRebateResultBO> result = ListUtil.toList();
        //查询采购关系
        List<ProcurementRelationDO> relations = relationService.queryInProRelationListByEachOther(request.getSellerEid(), request.getBuyerEid());
        if (CollUtil.isEmpty(relations)) {
            return result;
        }
        //查询采购关系下的商品
        List<ProcurementRelationGoodsDO> relationGoodsList = goodsService.queryGoodsByRelationIdAndGoodsName(relations.stream().map(ProcurementRelationDO::getId).collect(Collectors.toList()), null);
        if (CollUtil.isEmpty(relationGoodsList) || CollUtil.isEmpty(request.getGoodsList())) {
            return result;
        }
        //返利优惠大于0的商品
        Map<Long, ProcurementRelationDO> relationDOMap = relations.stream().collect(Collectors.toMap(ProcurementRelationDO::getId, e -> e));
        Map<Long, ProcurementRelationGoodsDO> rebateGoodsMap = relationGoodsList.stream().filter(e -> e.getRebate().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toMap(ProcurementRelationGoodsDO::getGoodsId, e -> e));
        List<CalculateProcRelationRebateRequest.CalculateProcRelationGoodsRequest> goodsList = request.getGoodsList();

        goodsList.forEach(e -> {
            ProcurementRelationGoodsDO rebateGoods = rebateGoodsMap.get(e.getGoodsId());
            if (ObjectUtil.isNull(rebateGoods)) {
                return;
            }
            ProcRelationRebateResultBO var = new ProcRelationRebateResultBO();
            var.setRelationId(relationDOMap.get(rebateGoods.getRelationId()).getId());
            var.setVersionId(relationDOMap.get(rebateGoods.getRelationId()).getVersionId());
            var.setRebateAmount(rebateGoods.getRebate().divide(new BigDecimal(100)).multiply(e.getGoodsAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
            var.setGoodsAmount(e.getGoodsAmount());
            var.setGoodsId(e.getGoodsId());
            var.setRebate(rebateGoods.getRebate());
            result.add(var);
        });

        return result;
    }

}
