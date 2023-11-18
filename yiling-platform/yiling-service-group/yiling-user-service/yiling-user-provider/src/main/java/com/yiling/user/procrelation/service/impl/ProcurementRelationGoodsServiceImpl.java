package com.yiling.user.procrelation.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.procrelation.dao.ProcurementRelationGoodsMapper;
import com.yiling.user.procrelation.dto.request.AddGoodsForProcRelationRequest;
import com.yiling.user.procrelation.dto.request.QueryRelationGoodsPageRequest;
import com.yiling.user.procrelation.dto.request.QuerySpecByBuyerPageRequest;
import com.yiling.user.procrelation.entity.ProcurementRelationGoodsDO;
import com.yiling.user.procrelation.enums.ProcRelationErrorCode;
import com.yiling.user.procrelation.service.ProcurementRelationGoodsService;
import com.yiling.user.procrelation.service.ProcurementRelationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * pop采购关系的可采商品 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
@Slf4j
@Service
public class ProcurementRelationGoodsServiceImpl extends BaseServiceImpl<ProcurementRelationGoodsMapper, ProcurementRelationGoodsDO> implements ProcurementRelationGoodsService {

    @Autowired
    ProcurementRelationService procurementRelationService;

    @Override
    public List<ProcurementRelationGoodsDO> queryGoodsByRelationId(Long relationId) {
        if (ObjectUtil.isNull(relationId) || ObjectUtil.equal(0L, relationId)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<ProcurementRelationGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcurementRelationGoodsDO::getRelationId, relationId);
        return list(wrapper);
    }

    @Override
    public List<ProcurementRelationGoodsDO> queryGoodsByRelationIdAndGoodsName(List<Long> relationIds, String goodsName) {
        if (CollUtil.isEmpty(relationIds)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<ProcurementRelationGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ProcurementRelationGoodsDO::getRelationId, relationIds);
        wrapper.like(StrUtil.isNotBlank(goodsName), ProcurementRelationGoodsDO::getGoodsName, goodsName);

        return list(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addGoodsForProcRelation(List<AddGoodsForProcRelationRequest> requestList) {
        if (CollUtil.isEmpty(requestList)) {
            log.warn("为POP采购关系新增商品时，传入的参数为空");
            return Boolean.FALSE;
        }
        Long relationId = requestList.stream().findAny().get().getRelationId();

        //校验新增的商品目前是否存在
        List<Long> goodsIdList = requestList.stream().filter(e -> ObjectUtil.isNull(e.getId()) || ObjectUtil.equal(0L, e.getId())).map(AddGoodsForProcRelationRequest::getGoodsId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(goodsIdList)) {
            LambdaQueryWrapper<ProcurementRelationGoodsDO> addCheckWrapper = Wrappers.lambdaQuery();
            addCheckWrapper.eq(ProcurementRelationGoodsDO::getRelationId, relationId);
            addCheckWrapper.in(ProcurementRelationGoodsDO::getGoodsId, goodsIdList);

            List<ProcurementRelationGoodsDO> alreadyGoods = list(addCheckWrapper);
            if (CollUtil.isNotEmpty(alreadyGoods)) {
                String errMsg = alreadyGoods.stream().map(item -> item.getGoodsName() + "-" + item.getSellSpecifications() + ",").collect(Collectors.joining());
                log.warn("为POP采购关系新增商品时，传入的商品目前已存在，采购关系id={}，存在的商品为{}", relationId, errMsg);
                throw new BusinessException(ProcRelationErrorCode.RELATION_GOODS_ALREADY, errMsg);
            }
        }
        //校验更新的商品是否存在
        List<Long> updateIdList = requestList.stream().filter(e -> ObjectUtil.isNotNull(e.getId()) && ObjectUtil.notEqual(0L, e.getId())).map(AddGoodsForProcRelationRequest::getId).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(updateIdList)) {
            List<ProcurementRelationGoodsDO> oldGoodsList = listByIds(updateIdList);
            //如果长度不一致
            if (ObjectUtil.notEqual(oldGoodsList.size(), updateIdList.size())) {
                List<Long> tempIdList = new ArrayList<>();
                Map<Long, ProcurementRelationGoodsDO> goodsDOMap = oldGoodsList.stream().collect(Collectors.toMap(ProcurementRelationGoodsDO::getId, e -> e));
                updateIdList.forEach(e -> {
                    if (!goodsDOMap.containsKey(e)) {
                        tempIdList.add(e);
                    }
                });
                String errMsg = "更新商品优惠折扣时id为" + StrUtil.join("，", tempIdList) + "的数据不存在";
                log.warn("为POP采购关系更新商品折扣时，数据不存在，采购关系id={}，商品关联关系id={}", relationId, tempIdList);
                throw new BusinessException(ProcRelationErrorCode.RELATION_GOODS_NOT_FIND, errMsg);
            }
        }

        //存入采购关系商品表
        List<ProcurementRelationGoodsDO> list = PojoUtils.map(requestList, ProcurementRelationGoodsDO.class);
        boolean isSuccess = saveOrUpdateBatch(list);
        if (!isSuccess) {
            log.error("为POP采购关系新增商品时,保存商品失败，参数={}", list);
            throw new ServiceException(ResultCode.FAILED);
        }
        //升级采购关系版本号
        procurementRelationService.increaseVersion(relationId, requestList.stream().findAny().get().getOpUserId());
        return Boolean.TRUE;
    }

    @Override
    public Page<ProcurementRelationGoodsDO> queryProcRelationGoodsPage(QueryRelationGoodsPageRequest request) {
        if (ObjectUtil.isNull(request.getRelationId()) || ObjectUtil.equal(0L, request.getRelationId())) {
            return request.getPage();
        }
        LambdaQueryWrapper<ProcurementRelationGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcurementRelationGoodsDO::getRelationId, request.getRelationId());
        return page(request.getPage(), wrapper);
    }

    @Override
    public Page<Long> queryGoodsPageByBuyer(QuerySpecByBuyerPageRequest request) {
        if (null == request.getBuyerEid() || request.getBuyerEid() == 0) {
            throw new BusinessException(ResultCode.FAILED, "采购商为空");
        }
        return this.baseMapper.queryGoodsPageByBuyer(request.getPage(), request);
    }

    @Override
    public Boolean emptyGoodsByRelationId(Long relationId, Long opUserId) {
        if (ObjectUtil.isNull(relationId) || ObjectUtil.equal(0L, relationId)) {
            return Boolean.FALSE;
        }
        LambdaQueryWrapper<ProcurementRelationGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcurementRelationGoodsDO::getRelationId, relationId);
        List<ProcurementRelationGoodsDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return Boolean.TRUE;
        }
        ProcurementRelationGoodsDO var = new ProcurementRelationGoodsDO();
        var.setOpTime(new Date());
        var.setOpUserId(opUserId);
        int row = batchDeleteWithFill(null, wrapper);
        return row > 0;
    }
}
