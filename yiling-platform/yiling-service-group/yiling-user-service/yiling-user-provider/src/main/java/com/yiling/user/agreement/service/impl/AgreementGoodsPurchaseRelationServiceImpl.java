package com.yiling.user.agreement.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.agreement.dao.AgreementGoodsPurchaseRelationMapper;
import com.yiling.user.agreement.dto.request.EditAgreementGoodsPurchaseRelationRequest;
import com.yiling.user.agreement.entity.AgreementGoodsPurchaseRelationDO;
import com.yiling.user.agreement.service.AgreementGoodsPurchaseRelationService;
import com.yiling.user.agreement.service.AgreementGoodsService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;

/**
 * <p>
 * 商品统计表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-09
 */
@Service
public class AgreementGoodsPurchaseRelationServiceImpl extends BaseServiceImpl<AgreementGoodsPurchaseRelationMapper, AgreementGoodsPurchaseRelationDO> implements AgreementGoodsPurchaseRelationService {

    @Autowired
    AgreementGoodsService agreementGoodsService;

    @Override
    public Boolean updateBuyerGatherByGid(EditAgreementGoodsPurchaseRelationRequest request) {
        //先判断是否存在原始数据
        QueryWrapper<AgreementGoodsPurchaseRelationDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AgreementGoodsPurchaseRelationDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AgreementGoodsPurchaseRelationDO::getGoodsId, request.getGoodsId());

        AgreementGoodsPurchaseRelationDO agreementGoodsPurchaseRelationDO = new AgreementGoodsPurchaseRelationDO();
        agreementGoodsPurchaseRelationDO.setOpUserId(request.getOpUserId());
        agreementGoodsPurchaseRelationDO.setBuyerGather(request.getBuyerGather());
        return this.update(agreementGoodsPurchaseRelationDO, queryWrapper);
    }

    @Override
    public Boolean updateBuyerGatherByGid(List<Long> goodsIds, Long buyerEid, Long opUserId) {
        if (CollectionUtil.isEmpty(goodsIds)) {
            return false;
        }

        List<AgreementGoodsPurchaseRelationDO> insert = new ArrayList<>();
        List<AgreementGoodsPurchaseRelationDO> update = new ArrayList<>();
        QueryWrapper<AgreementGoodsPurchaseRelationDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AgreementGoodsPurchaseRelationDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(AgreementGoodsPurchaseRelationDO::getGoodsId, goodsIds);

        List<AgreementGoodsPurchaseRelationDO> agreementGoodsPurchaseRelationDOList = this.list(queryWrapper);
        Map<Long, AgreementGoodsPurchaseRelationDO> map = agreementGoodsPurchaseRelationDOList.stream().collect(Collectors.toMap(AgreementGoodsPurchaseRelationDO::getGoodsId, Function.identity()));
        //判读是否在关系里面
        for (Long goodsId : goodsIds) {
            //需要修改
            if (map.containsKey(goodsId)) {
                AgreementGoodsPurchaseRelationDO agreementGoodsPurchaseRelationDO = map.get(goodsId);
                if (Arrays.asList(agreementGoodsPurchaseRelationDO.getBuyerGather().split(",")).contains(String.valueOf(buyerEid))) {
                    continue;
                }
                //需要添加
                String buyerGather = agreementGoodsPurchaseRelationDO.getBuyerGather();
                agreementGoodsPurchaseRelationDO.setBuyerGather(buyerGather + "," + buyerEid);
                agreementGoodsPurchaseRelationDO.setOpUserId(opUserId);
                update.add(agreementGoodsPurchaseRelationDO);
            } else {
                //需要添加
                AgreementGoodsPurchaseRelationDO agreementGoodsPurchaseRelationDO = new AgreementGoodsPurchaseRelationDO();
                agreementGoodsPurchaseRelationDO.setGoodsId(goodsId);
                agreementGoodsPurchaseRelationDO.setBuyerGather(String.valueOf(buyerEid));
                agreementGoodsPurchaseRelationDO.setOpUserId(opUserId);
                insert.add(agreementGoodsPurchaseRelationDO);
            }
        }

        if (CollUtil.isNotEmpty(insert)) {
            this.saveBatch(insert);
        }
        if (CollUtil.isNotEmpty(update)) {
            this.updateBatchById(update);
        }
        return true;
    }

    @Override
    public Boolean deleteBuyerGatherByGid(List<Long> goodsIds, Long buyerEid, Long opUserId) {
        if (CollectionUtil.isEmpty(goodsIds)) {
            return false;
        }
        List<AgreementGoodsPurchaseRelationDO> delete = new ArrayList<>();
        QueryWrapper<AgreementGoodsPurchaseRelationDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AgreementGoodsPurchaseRelationDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(AgreementGoodsPurchaseRelationDO::getGoodsId, goodsIds);

        List<AgreementGoodsPurchaseRelationDO> agreementGoodsPurchaseRelationDOList = this.list(queryWrapper);
        Map<Long, AgreementGoodsPurchaseRelationDO> map = agreementGoodsPurchaseRelationDOList.stream().collect(Collectors.toMap(AgreementGoodsPurchaseRelationDO::getGoodsId, Function.identity()));
        //判读是否在关系里面
        for (Long goodsId : goodsIds) {
            //需要修改
            if (map.containsKey(goodsId)) {
                AgreementGoodsPurchaseRelationDO agreementGoodsPurchaseRelationDO = map.get(goodsId);
                List<String> list = new ArrayList<>(Arrays.asList(agreementGoodsPurchaseRelationDO.getBuyerGather().split(",")));
                if (list.contains(String.valueOf(buyerEid))) {
                    list.remove(String.valueOf(buyerEid));
                    agreementGoodsPurchaseRelationDO.setBuyerGather(StringUtils.join(list, ","));
                    agreementGoodsPurchaseRelationDO.setOpUserId(opUserId);
                    delete.add(agreementGoodsPurchaseRelationDO);
                }
            }
        }

        if (CollUtil.isNotEmpty(delete)) {
            this.updateBatchById(delete);
        }
        return true;
    }
}
