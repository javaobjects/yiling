package com.yiling.marketing.strategy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.marketing.payPromotion.entity.MarketingPayPlatformGoodsLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionSellerLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayPlatformGoodsLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionSellerLimitService;
import com.yiling.marketing.strategy.dao.StrategyPlatformGoodsLimitMapper;
import com.yiling.marketing.strategy.dto.request.AddStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPlatformGoodsLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyPlatformGoodsLimitDO;
import com.yiling.marketing.strategy.enums.StrategyErrorCode;
import com.yiling.marketing.strategy.service.StrategyPlatformGoodsLimitService;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 策略满赠平台SKU 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Slf4j
@Service
public class StrategyPlatformGoodsLimitServiceImpl extends BaseServiceImpl<StrategyPlatformGoodsLimitMapper, StrategyPlatformGoodsLimitDO> implements StrategyPlatformGoodsLimitService {

    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    @Autowired
    MarketingPayPlatformGoodsLimitService payPlatformGoodsLimitService;

    @Override
    public List<Long> listSellSpecificationsIdByStrategyId(Long strategyActivityId, List<Long> sellSpecificationsIdList) {
        return this.getBaseMapper().listSellSpecificationsIdByStrategyId(strategyActivityId, sellSpecificationsIdList);
    }

    @Autowired
    MarketingPayPlatformGoodsLimitService marketingPayPlatformGoodsLimitService;
    @Override
    public boolean add(AddStrategyPlatformGoodsLimitRequest request) {
        if (Objects.nonNull(request.getSellSpecificationsId())) {
            StrategyPlatformGoodsLimitDO strategyPlatformGoodsLimitDO = this.queryByActivityIdAndSellSpecificationsId(request.getMarketingStrategyId(), request.getSellSpecificationsId());
            if (Objects.nonNull(strategyPlatformGoodsLimitDO)) {
                // 已经存在，抛出异常
                return false;
            }
            StrategyPlatformGoodsLimitDO platformGoodsLimitDO = PojoUtils.map(request, StrategyPlatformGoodsLimitDO.class);
            StandardGoodsSpecificationDTO standardGoodsSpecification = standardGoodsSpecificationApi.getStandardGoodsSpecification(request.getSellSpecificationsId());
            platformGoodsLimitDO.setStandardId(standardGoodsSpecification.getStandardId());
            platformGoodsLimitDO.setGoodsName(standardGoodsSpecification.getName());
            return this.save(platformGoodsLimitDO);
        } else if (CollUtil.isNotEmpty(request.getSellSpecificationsIdList())) {
            List<StandardGoodsSpecificationDTO> standardGoodsSpecificationDTOList = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(request.getSellSpecificationsIdList());
            List<StrategyPlatformGoodsLimitDO> platformGoodsLimitDOList = new ArrayList<>();
            for (StandardGoodsSpecificationDTO standardGoodsSpecificationDTO : standardGoodsSpecificationDTOList) {
                StrategyPlatformGoodsLimitDO strategyPlatformGoodsLimitDO = this.queryByActivityIdAndSellSpecificationsId(request.getMarketingStrategyId(), standardGoodsSpecificationDTO.getId());
                if (Objects.nonNull(strategyPlatformGoodsLimitDO)) {
                    // 已经存在，进入下一个
                    continue;
                }
                StrategyPlatformGoodsLimitDO platformGoodsLimitDO = new StrategyPlatformGoodsLimitDO();
                platformGoodsLimitDO.setMarketingStrategyId(request.getMarketingStrategyId());
                platformGoodsLimitDO.setStandardId(standardGoodsSpecificationDTO.getStandardId());
                platformGoodsLimitDO.setSellSpecificationsId(standardGoodsSpecificationDTO.getId());
                platformGoodsLimitDO.setGoodsName(standardGoodsSpecificationDTO.getName());
                platformGoodsLimitDOList.add(platformGoodsLimitDO);
            }
            if (CollUtil.isNotEmpty(platformGoodsLimitDOList)) {
                return this.saveBatch(platformGoodsLimitDOList);
            }
        } else {
            StandardSpecificationPageRequest pageRequest = new StandardSpecificationPageRequest();
            if (Objects.nonNull(request.getStandardIdPage())) {
                pageRequest.setStandardId(request.getStandardIdPage());
            }
            if (Objects.nonNull(request.getSellSpecificationsIdPage())) {
                pageRequest.setSpecIdList(new ArrayList<Long>() {{
                    add(request.getSellSpecificationsIdPage());
                }});
            }
            if (StringUtils.isNotBlank(request.getGoodsNamePage())) {
                pageRequest.setName(request.getGoodsNamePage());
            }
            if (StringUtils.isNotBlank(request.getManufacturerPage())) {
                pageRequest.setManufacturer(request.getManufacturerPage());
            }
            //0：非以岭  1：以岭
            if (request.getIsYiLing() == 1) {
                pageRequest.setYlFlag(1);
            }
            if (request.getIsYiLing() == 2) {
                pageRequest.setYlFlag(0);
            }
            Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage;
            int current = 1;
            do {
                pageRequest.setCurrent(current);
                pageRequest.setSize(500);
                specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
                if (CollUtil.isEmpty(specificationGoodsInfoPage.getRecords())) {
                    continue;
                }
                long total = specificationGoodsInfoPage.getTotal();
                if (total > 500L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_PLATFORM_GOODS_TO_MANY);
                }
                List<Long> specIdList = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
                List<StrategyPlatformGoodsLimitDO> strategyPlatformGoodsLimitDOList = this.listByActivityIdAndSellSpecificationsIdList(request.getMarketingStrategyId(), specIdList);
                List<Long> haveSellSpecificationsIdList = strategyPlatformGoodsLimitDOList.stream().map(StrategyPlatformGoodsLimitDO::getSellSpecificationsId).collect(Collectors.toList());
                List<StrategyPlatformGoodsLimitDO> platformGoodsLimitDOList = new ArrayList<>();
                for (StandardSpecificationGoodsInfoBO specificationGoodsInfoBO : specificationGoodsInfoPage.getRecords()) {
                    if (haveSellSpecificationsIdList.contains(specificationGoodsInfoBO.getId())) {
                        continue;
                    }
                    StrategyPlatformGoodsLimitDO platformGoodsLimitDO = new StrategyPlatformGoodsLimitDO();
                    platformGoodsLimitDO.setMarketingStrategyId(request.getMarketingStrategyId());
                    platformGoodsLimitDO.setStandardId(specificationGoodsInfoBO.getStandardId());
                    platformGoodsLimitDO.setSellSpecificationsId(specificationGoodsInfoBO.getId());
                    platformGoodsLimitDO.setGoodsName(specificationGoodsInfoBO.getName());
                    platformGoodsLimitDO.setOpUserId(request.getOpUserId());
                    platformGoodsLimitDO.setOpTime(request.getOpTime());
                    platformGoodsLimitDOList.add(platformGoodsLimitDO);
                }
                if (CollUtil.isNotEmpty(platformGoodsLimitDOList)) {
                    this.saveBatch(platformGoodsLimitDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(specificationGoodsInfoPage.getRecords()));
            return true;
        }
        return false;
    }



    public MarketingPayPlatformGoodsLimitDO queryPayByActivityIdAndSellSpecificationsId(Long strategyActivityId, Long sellSpecificationsId) {
        QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, strategyActivityId);
        wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId,sellSpecificationsId);
        wrapper.lambda().last(" limit 1");
        return marketingPayPlatformGoodsLimitService.getOne(wrapper);
    }

    @Override
    public boolean addForPayPromotion(AddStrategyPlatformGoodsLimitRequest request) {
        if (Objects.nonNull(request.getSellSpecificationsId())) {
            MarketingPayPlatformGoodsLimitDO strategyPlatformGoodsLimitDO = queryPayByActivityIdAndSellSpecificationsId(request.getMarketingStrategyId(), request.getSellSpecificationsId());
            if (Objects.nonNull(strategyPlatformGoodsLimitDO)) {
                // 已经存在，抛出异常
                return false;
            }
            StrategyPlatformGoodsLimitDO platformGoodsLimitDO = PojoUtils.map(request, StrategyPlatformGoodsLimitDO.class);
            StandardGoodsSpecificationDTO standardGoodsSpecification = standardGoodsSpecificationApi.getStandardGoodsSpecification(request.getSellSpecificationsId());
            platformGoodsLimitDO.setStandardId(standardGoodsSpecification.getStandardId());
            platformGoodsLimitDO.setGoodsName(standardGoodsSpecification.getName());
            return this.save(platformGoodsLimitDO);
        } else if (CollUtil.isNotEmpty(request.getSellSpecificationsIdList())) {
            List<StandardGoodsSpecificationDTO> standardGoodsSpecificationDTOList = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(request.getSellSpecificationsIdList());
            List<MarketingPayPlatformGoodsLimitDO> platformGoodsLimitDOList = new ArrayList<>();
            for (StandardGoodsSpecificationDTO standardGoodsSpecificationDTO : standardGoodsSpecificationDTOList) {
                MarketingPayPlatformGoodsLimitDO strategyPlatformGoodsLimitDO = this.queryPayByActivityIdAndSellSpecificationsId(request.getMarketingStrategyId(), standardGoodsSpecificationDTO.getId());
                if (Objects.nonNull(strategyPlatformGoodsLimitDO)) {
                    // 已经存在，进入下一个
                    continue;
                }
                MarketingPayPlatformGoodsLimitDO platformGoodsLimitDO = new MarketingPayPlatformGoodsLimitDO();
                platformGoodsLimitDO.setMarketingPayId(request.getMarketingStrategyId());
                platformGoodsLimitDO.setStandardId(standardGoodsSpecificationDTO.getStandardId());
                platformGoodsLimitDO.setSellSpecificationsId(standardGoodsSpecificationDTO.getId());
                platformGoodsLimitDO.setGoodsName(standardGoodsSpecificationDTO.getName());
                platformGoodsLimitDOList.add(platformGoodsLimitDO);
            }
            if (CollUtil.isNotEmpty(platformGoodsLimitDOList)) {
                return marketingPayPlatformGoodsLimitService.saveBatch(platformGoodsLimitDOList);
            }
        } else {
            StandardSpecificationPageRequest pageRequest = new StandardSpecificationPageRequest();
            if (Objects.nonNull(request.getStandardIdPage())) {
                pageRequest.setStandardId(request.getStandardIdPage());
            }
            if (Objects.nonNull(request.getSellSpecificationsIdPage())) {
                pageRequest.setSpecIdList(new ArrayList<Long>() {{
                    add(request.getSellSpecificationsIdPage());
                }});
            }
            if (StringUtils.isNotBlank(request.getGoodsNamePage())) {
                pageRequest.setName(request.getGoodsNamePage());
            }
            if (StringUtils.isNotBlank(request.getManufacturerPage())) {
                pageRequest.setManufacturer(request.getManufacturerPage());
            }
            //0：非以岭  1：以岭
            if (request.getIsYiLing() == 1) {
                pageRequest.setYlFlag(1);
            }
            if (request.getIsYiLing() == 2) {
                pageRequest.setYlFlag(0);
            }
            Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage;
            int current = 1;
            do {
                pageRequest.setCurrent(current);
                pageRequest.setSize(500);
                specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
                if (CollUtil.isEmpty(specificationGoodsInfoPage.getRecords())) {
                    continue;
                }
                long total = specificationGoodsInfoPage.getTotal();
                if (total > 1000L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_PLATFORM_GOODS_TO_MANY);
                }
                List<Long> specIdList = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
                List<MarketingPayPlatformGoodsLimitDO> strategyPlatformGoodsLimitDOList = this.listPayByActivityIdAndSellSpecificationsIdList(request.getMarketingStrategyId(), specIdList);
                List<Long> haveSellSpecificationsIdList = strategyPlatformGoodsLimitDOList.stream().map(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId).collect(Collectors.toList());
                List<MarketingPayPlatformGoodsLimitDO> platformGoodsLimitDOList = new ArrayList<>();
                for (StandardSpecificationGoodsInfoBO specificationGoodsInfoBO : specificationGoodsInfoPage.getRecords()) {
                    if (haveSellSpecificationsIdList.contains(specificationGoodsInfoBO.getId())) {
                        continue;
                    }
                    MarketingPayPlatformGoodsLimitDO platformGoodsLimitDO = new MarketingPayPlatformGoodsLimitDO();
                    platformGoodsLimitDO.setMarketingPayId(request.getMarketingStrategyId());
                    platformGoodsLimitDO.setStandardId(specificationGoodsInfoBO.getStandardId());
                    platformGoodsLimitDO.setSellSpecificationsId(specificationGoodsInfoBO.getId());
                    platformGoodsLimitDO.setGoodsName(specificationGoodsInfoBO.getName());
                    platformGoodsLimitDO.setOpUserId(request.getOpUserId());
                    platformGoodsLimitDO.setOpTime(request.getOpTime());
                    platformGoodsLimitDOList.add(platformGoodsLimitDO);
                }
                if (CollUtil.isNotEmpty(platformGoodsLimitDOList)) {
                    marketingPayPlatformGoodsLimitService.saveBatch(platformGoodsLimitDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(specificationGoodsInfoPage.getRecords()));
            return true;
        }
        return false;
    }

    public List<MarketingPayPlatformGoodsLimitDO> listPayByActivityIdAndSellSpecificationsIdList(Long strategyActivityId, List<Long> sellSpecificationsIdList) {
        QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, strategyActivityId);
        if (CollUtil.isNotEmpty(sellSpecificationsIdList)) {
            wrapper.lambda().in(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId, sellSpecificationsIdList);
        }
        return marketingPayPlatformGoodsLimitService.list(wrapper);
    }

    @Override
    public boolean deleteForPayPromotionPlatformSku(DeleteStrategyPlatformGoodsLimitRequest request) {
        MarketingPayPlatformGoodsLimitDO platformGoodsLimitDO = new MarketingPayPlatformGoodsLimitDO();
        platformGoodsLimitDO.setDelFlag(1);
        platformGoodsLimitDO.setOpUserId(request.getOpUserId());
        platformGoodsLimitDO.setOpTime(request.getOpTime());
        if (Objects.nonNull(request.getSellSpecificationsId())) {
            QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
            wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId, request.getSellSpecificationsId());
            marketingPayPlatformGoodsLimitService.batchDeleteWithFill(platformGoodsLimitDO, wrapper);
        } else if (CollUtil.isNotEmpty(request.getSellSpecificationsIdList())) {
            QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
            wrapper.lambda().in(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId, request.getSellSpecificationsIdList());
            marketingPayPlatformGoodsLimitService.batchDeleteWithFill(platformGoodsLimitDO, wrapper);
        } else {
            StandardSpecificationPageRequest pageRequest = new StandardSpecificationPageRequest();
            if (Objects.nonNull(request.getStandardIdPage())) {
                pageRequest.setStandardId(request.getStandardIdPage());
            }
            if (Objects.nonNull(request.getSellSpecificationsIdPage())) {
                pageRequest.setSpecIdList(new ArrayList<Long>() {{
                    add(request.getSellSpecificationsIdPage());
                }});
            }
            if (StringUtils.isNotBlank(request.getGoodsNamePage())) {
                pageRequest.setName(request.getGoodsNamePage());
            }
            if (StringUtils.isNotBlank(request.getManufacturerPage())) {
                pageRequest.setManufacturer(request.getManufacturerPage());
            }
            //0：非以岭  1：以岭
            if (request.getIsYiLing() == 1) {
                pageRequest.setYlFlag(1);
            }
            if (request.getIsYiLing() == 2) {
                pageRequest.setYlFlag(0);
            }
            Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage;
            int current = 1;
            do {
                pageRequest.setCurrent(current);
                pageRequest.setSize(500);
                specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
                if (CollUtil.isEmpty(specificationGoodsInfoPage.getRecords())) {
                    continue;
                }
                long total = specificationGoodsInfoPage.getTotal();
                if (total > 500L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_PLATFORM_GOODS_TO_MANY);
                }
                List<Long> delSellSpecificationsIdList = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
                QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
                wrapper.lambda().in(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId, delSellSpecificationsIdList);
                marketingPayPlatformGoodsLimitService.batchDeleteWithFill(platformGoodsLimitDO, wrapper);
                current = current + 1;
            } while (CollUtil.isNotEmpty(specificationGoodsInfoPage.getRecords()));
        }
        return true;
    }

    @Override
    public boolean delete(DeleteStrategyPlatformGoodsLimitRequest request) {
        StrategyPlatformGoodsLimitDO platformGoodsLimitDO = new StrategyPlatformGoodsLimitDO();
        platformGoodsLimitDO.setDelFlag(1);
        platformGoodsLimitDO.setOpUserId(request.getOpUserId());
        platformGoodsLimitDO.setOpTime(request.getOpTime());
        if (Objects.nonNull(request.getSellSpecificationsId())) {
            QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
            wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getSellSpecificationsId, request.getSellSpecificationsId());
            this.batchDeleteWithFill(platformGoodsLimitDO, wrapper);
        } else if (CollUtil.isNotEmpty(request.getSellSpecificationsIdList())) {
            QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
            wrapper.lambda().in(StrategyPlatformGoodsLimitDO::getSellSpecificationsId, request.getSellSpecificationsIdList());
            this.batchDeleteWithFill(platformGoodsLimitDO, wrapper);
        } else {
            StandardSpecificationPageRequest pageRequest = new StandardSpecificationPageRequest();
            if (Objects.nonNull(request.getStandardIdPage())) {
                pageRequest.setStandardId(request.getStandardIdPage());
            }
            if (Objects.nonNull(request.getSellSpecificationsIdPage())) {
                pageRequest.setSpecIdList(new ArrayList<Long>() {{
                    add(request.getSellSpecificationsIdPage());
                }});
            }
            if (StringUtils.isNotBlank(request.getGoodsNamePage())) {
                pageRequest.setName(request.getGoodsNamePage());
            }
            if (StringUtils.isNotBlank(request.getManufacturerPage())) {
                pageRequest.setManufacturer(request.getManufacturerPage());
            }
            //0：非以岭  1：以岭
            if (request.getIsYiLing() == 1) {
                pageRequest.setYlFlag(1);
            }
            if (request.getIsYiLing() == 2) {
                pageRequest.setYlFlag(0);
            }
            Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage;
            int current = 1;
            do {
                pageRequest.setCurrent(current);
                pageRequest.setSize(500);
                specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
                if (CollUtil.isEmpty(specificationGoodsInfoPage.getRecords())) {
                    continue;
                }
                long total = specificationGoodsInfoPage.getTotal();
                if (total > 500L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_PLATFORM_GOODS_TO_MANY);
                }
                List<Long> delSellSpecificationsIdList = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
                QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
                wrapper.lambda().in(StrategyPlatformGoodsLimitDO::getSellSpecificationsId, delSellSpecificationsIdList);
                this.batchDeleteWithFill(platformGoodsLimitDO, wrapper);
                current = current + 1;
            } while (CollUtil.isNotEmpty(specificationGoodsInfoPage.getRecords()));
        }
        return true;
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        Page<StrategyPlatformGoodsLimitDO> platformGoodsLimitDOPage;
        QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, oldId);
        int current = 1;
        do {
            Page<StrategyPlatformGoodsLimitDO> objectPage = new Page<>(current, 100);
            platformGoodsLimitDOPage = this.page(objectPage, wrapper);
            if (CollUtil.isEmpty(platformGoodsLimitDOPage.getRecords())) {
                continue;
            }
            List<StrategyPlatformGoodsLimitDO> platformGoodsLimitDOList = platformGoodsLimitDOPage.getRecords();
            for (StrategyPlatformGoodsLimitDO platformGoodsLimitDO : platformGoodsLimitDOList) {
                platformGoodsLimitDO.setId(null);
                platformGoodsLimitDO.setMarketingStrategyId(strategyActivityDO.getId());
                platformGoodsLimitDO.setOpUserId(opUserId);
                platformGoodsLimitDO.setOpTime(opTime);
            }
            if (CollUtil.isNotEmpty(platformGoodsLimitDOList)) {
                this.saveBatch(platformGoodsLimitDOList);
            }
            current = current + 1;
        } while (CollUtil.isNotEmpty(platformGoodsLimitDOPage.getRecords()));
        log.info("指定平台SKU 复制完成!");
    }

    @Override
    public Page<MarketingPayPlatformGoodsLimitDO> pageListFroPayPromotion(QueryStrategyPlatformGoodsLimitPageRequest request) {
        QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getStandardId())) {
            wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getStandardId, request.getStandardId());
        }
        if (Objects.nonNull(request.getSellSpecificationsId())) {
            wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId, request.getSellSpecificationsId());
        }
        List<MarketingPayPlatformGoodsLimitDO> platformGoodsLimitDOList = marketingPayPlatformGoodsLimitService.list(wrapper);
        if (CollUtil.isEmpty(platformGoodsLimitDOList)) {
            return new Page<>();
        }
        List<Long> specIdList = platformGoodsLimitDOList.stream().map(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId).collect(Collectors.toList());
        StandardSpecificationPageRequest pageRequest = PojoUtils.map(request, StandardSpecificationPageRequest.class);
        pageRequest.setSpecIdList(specIdList);
        pageRequest.setName(request.getGoodsName());
        //0：非以岭  1：以岭
        if (request.getIsYiLing() == 1) {
            pageRequest.setYlFlag(1);
        }
        if (request.getIsYiLing() == 2) {
            pageRequest.setYlFlag(0);
        }
        Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
        Page<MarketingPayPlatformGoodsLimitDO> doPage = PojoUtils.map(specificationGoodsInfoPage, MarketingPayPlatformGoodsLimitDO.class);
        if (CollUtil.isEmpty(doPage.getRecords())) {
            return new Page<>();
        }
        List<Long> sellSpecificationsIdList = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
        QueryWrapper<MarketingPayPlatformGoodsLimitDO> goodsLimitDOQueryWrapper = new QueryWrapper<>();
        goodsLimitDOQueryWrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        if (CollUtil.isNotEmpty(sellSpecificationsIdList)) {
            goodsLimitDOQueryWrapper.lambda().in(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId, sellSpecificationsIdList);
        }
        List<MarketingPayPlatformGoodsLimitDO> strategyPlatformGoodsLimitDOList = marketingPayPlatformGoodsLimitService.list(goodsLimitDOQueryWrapper);
        doPage.setRecords(strategyPlatformGoodsLimitDOList);
        return doPage;
    }

    @Override
    public Page<StrategyPlatformGoodsLimitDO> pageList(QueryStrategyPlatformGoodsLimitPageRequest request) {
        QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getStandardId())) {
            wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getStandardId, request.getStandardId());
        }
        if (Objects.nonNull(request.getSellSpecificationsId())) {
            wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getSellSpecificationsId, request.getSellSpecificationsId());
        }
        List<StrategyPlatformGoodsLimitDO> platformGoodsLimitDOList = this.list(wrapper);
        if (CollUtil.isEmpty(platformGoodsLimitDOList)) {
            return new Page<>();
        }
        List<Long> specIdList = platformGoodsLimitDOList.stream().map(StrategyPlatformGoodsLimitDO::getSellSpecificationsId).collect(Collectors.toList());
        StandardSpecificationPageRequest pageRequest = PojoUtils.map(request, StandardSpecificationPageRequest.class);
        pageRequest.setSpecIdList(specIdList);
        pageRequest.setName(request.getGoodsName());
        //0：非以岭  1：以岭
        if (request.getIsYiLing() == 1) {
            pageRequest.setYlFlag(1);
        }
        if (request.getIsYiLing() == 2) {
            pageRequest.setYlFlag(0);
        }
        Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
        Page<StrategyPlatformGoodsLimitDO> doPage = PojoUtils.map(specificationGoodsInfoPage, StrategyPlatformGoodsLimitDO.class);
        if (CollUtil.isEmpty(doPage.getRecords())) {
            return new Page<>();
        }
        List<Long> sellSpecificationsIdPage = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
        List<StrategyPlatformGoodsLimitDO> strategyPlatformGoodsLimitDOList = this.listByActivityIdAndSellSpecificationsIdList(request.getMarketingStrategyId(), sellSpecificationsIdPage);
        doPage.setRecords(strategyPlatformGoodsLimitDOList);
        return doPage;
    }

    @Override
    public Integer countPlatformGoodsByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.count(wrapper);
    }

    @Override
    public List<StrategyPlatformGoodsLimitDO> listPlatformGoodsByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.list(wrapper);
    }

    @Override
    public StrategyPlatformGoodsLimitDO queryByActivityIdAndSellSpecificationsId(Long strategyActivityId, Long sellSpecificationsId) {
        QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getSellSpecificationsId, sellSpecificationsId);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<StrategyPlatformGoodsLimitDO> listByActivityIdAndSellSpecificationsIdList(Long strategyActivityId, List<Long> sellSpecificationsIdList) {
        QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        if (CollUtil.isNotEmpty(sellSpecificationsIdList)) {
            wrapper.lambda().in(StrategyPlatformGoodsLimitDO::getSellSpecificationsId, sellSpecificationsIdList);
        }
        return this.list(wrapper);
    }

    @Override
    public List<StrategyPlatformGoodsLimitDO> listByActivityIdAndStandardIdList(Long strategyActivityId, List<Long> standardIdList) {
        QueryWrapper<StrategyPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPlatformGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        if (CollUtil.isNotEmpty(standardIdList)) {
            wrapper.lambda().in(StrategyPlatformGoodsLimitDO::getStandardId, standardIdList);
        }
        return this.list(wrapper);
    }

    @Override
    public Integer countPlatformGoodsByActivityIdForPayPromotion(Long strategyActivityId) {
        QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, strategyActivityId);
        return payPlatformGoodsLimitService.count(wrapper);
    }

}
