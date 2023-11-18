package com.yiling.cms.evaluate.api.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.cms.evaluate.api.HealthEvaluateMarketApi;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketAdviceDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketGoodsDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketPromoteDTO;
import com.yiling.cms.evaluate.dto.request.*;
import com.yiling.cms.evaluate.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 健康测评API
 *
 * @author: fan.shen
 * @date: 2022-12-06
 */
@Slf4j
@DubboService
public class HealthEvaluateMarketApiImpl implements HealthEvaluateMarketApi {

    @Autowired
    HealthEvaluateMarketGoodsService goodsService;

    @Autowired
    HealthEvaluateMarketAdviceService adviceService;

    @Autowired
    HealthEvaluateMarketPromoteService promoteService;

    @Override
    @Transactional
    public Boolean marketSet(HealthEvaluateMarketRequest request) {
        if (CollUtil.isNotEmpty(request.getGoodsList())) {
            request.getGoodsList().forEach(goods -> {
                goods.setOpUserId(request.getOpUserId());
                goodsService.healthEvaluateMarketGoods(goods);
            });
        }
        if (CollUtil.isNotEmpty(request.getAdviceList())) {
            request.getAdviceList().forEach(advice -> {
                advice.setOpUserId(request.getOpUserId());
                adviceService.healthEvaluateMarketAdvice(advice);
            });
        }
        if (CollUtil.isNotEmpty(request.getPromoteList())) {
            request.getPromoteList().forEach(promote -> {
                promote.setOpUserId(request.getOpUserId());
                promoteService.healthEvaluateMarketPromote(promote);
            });
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delMarket(DelHealthEvaluateMarketRequest request) {
        if (request.getType() == 1) {
            return goodsService.delHealthEvaluateMarketGoodsById(request.getId());
        }
        if (request.getType() == 2) {
            return adviceService.delHealthEvaluateMarketAdviceById(request.getId());
        }
        if (request.getType() == 3) {
            return promoteService.delHealthEvaluateMarketPromoteById(request.getId());
        }
        return Boolean.FALSE;
    }

    @Override
    public HealthEvaluateMarketDTO getMarketListByEvaluateId(Long evaluateId) {
        HealthEvaluateMarketDTO marketDTO = new HealthEvaluateMarketDTO();
        List<HealthEvaluateMarketGoodsDTO> goodsList = goodsService.getMarketGoodsByEvaluateId(evaluateId);
        List<HealthEvaluateMarketAdviceDTO> adviceList = adviceService.getMarketAdviceByEvaluateId(evaluateId);
        List<HealthEvaluateMarketPromoteDTO> promoteList = promoteService.getMarketPromoteByEvaluateId(evaluateId);
        marketDTO.setGoodsList(goodsList);
        marketDTO.setAdviceList(adviceList);
        marketDTO.setPromoteList(promoteList);
        log.info("[getMarketListByEvaluateId]marketDTO:{}", JSONUtil.toJsonStr(marketDTO));
        return marketDTO;
    }

    //
    // @Override
    // public Boolean updateHealthEvaluateResult(UpdateHealthEvaluateResultRequest request) {
    //     return healthEvaluateResultService.updateHealthEvaluateResult(request);
    // }
    //
    // @Override
    // public Boolean delHealthEvaluateResultById(Long id) {
    //     return healthEvaluateResultService.delHealthEvaluateResultById(id);
    // }
    //
    // @Override
    // public List<HealthEvaluateResultDTO> getResultListByEvaluateId(Long evaluateId) {
    //     return healthEvaluateResultService.getResultListByEvaluateId(evaluateId);
    // }
}