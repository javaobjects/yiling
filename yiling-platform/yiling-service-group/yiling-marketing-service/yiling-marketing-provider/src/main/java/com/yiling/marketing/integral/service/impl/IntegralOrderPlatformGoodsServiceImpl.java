package com.yiling.marketing.integral.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.marketing.integral.dao.IntegralOrderPlatformGoodsMapper;
import com.yiling.marketing.integral.entity.IntegralGiveRuleDO;
import com.yiling.marketing.integral.entity.IntegralOrderPlatformGoodsDO;
import com.yiling.marketing.integral.service.IntegralOrderPlatformGoodsService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.integral.dto.request.AddIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGivePlatformGoodsRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.dto.request.QueryIntegralGivePlatformGoodsPageRequest;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单送积分平台SKU表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralOrderPlatformGoodsServiceImpl extends BaseServiceImpl<IntegralOrderPlatformGoodsMapper, IntegralOrderPlatformGoodsDO> implements IntegralOrderPlatformGoodsService {

    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(AddIntegralGivePlatformGoodsRequest request) {
        if (Objects.nonNull(request.getSellSpecificationsId())) {
            IntegralOrderPlatformGoodsDO platformGoodsDO = this.queryByRuleIdAndSellSpecificationsId(request.getGiveRuleId(), request.getSellSpecificationsId());
            if (Objects.nonNull(platformGoodsDO)) {
                // 已经存在，抛出异常
                return true;
            }
            IntegralOrderPlatformGoodsDO platformGoodsLimitDO = PojoUtils.map(request, IntegralOrderPlatformGoodsDO.class);
            StandardGoodsSpecificationDTO standardGoodsSpecification = standardGoodsSpecificationApi.getStandardGoodsSpecification(request.getSellSpecificationsId());
            platformGoodsLimitDO.setStandardId(standardGoodsSpecification.getStandardId());
            this.save(platformGoodsLimitDO);

        } else if (CollUtil.isNotEmpty(request.getSellSpecificationsIdList())) {
            List<StandardGoodsSpecificationDTO> standardGoodsSpecificationDTOList = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(request.getSellSpecificationsIdList());
            List<IntegralOrderPlatformGoodsDO> platformGoodsLimitList = ListUtil.toList();
            for (StandardGoodsSpecificationDTO standardGoodsSpecificationDTO : standardGoodsSpecificationDTOList) {
                IntegralOrderPlatformGoodsDO platformGoodsLimitDO = this.queryByRuleIdAndSellSpecificationsId(request.getGiveRuleId(), standardGoodsSpecificationDTO.getId());
                if (Objects.nonNull(platformGoodsLimitDO)) {
                    continue;
                }
                IntegralOrderPlatformGoodsDO platformGoodsDO = new IntegralOrderPlatformGoodsDO();
                platformGoodsDO.setGiveRuleId(request.getGiveRuleId());
                platformGoodsDO.setStandardId(standardGoodsSpecificationDTO.getStandardId());
                platformGoodsDO.setSellSpecificationsId(standardGoodsSpecificationDTO.getId());
                platformGoodsDO.setOpUserId(request.getOpUserId());
                platformGoodsLimitList.add(platformGoodsDO);
            }
            if (CollUtil.isNotEmpty(platformGoodsLimitList)) {
                this.saveBatch(platformGoodsLimitList);
            }

        } else {
            StandardSpecificationPageRequest pageRequest = new StandardSpecificationPageRequest();
            if (Objects.nonNull(request.getStandardIdPage())) {
                pageRequest.setStandardId(request.getStandardIdPage());
            }
            if (Objects.nonNull(request.getSellSpecificationsIdPage())) {
                pageRequest.setSpecIdList(ListUtil.toList(request.getSellSpecificationsIdPage()));
            }
            if (StrUtil.isNotEmpty(request.getGoodsNamePage())) {
                pageRequest.setName(request.getGoodsNamePage());
            }
            if (StrUtil.isNotEmpty(request.getManufacturerPage())) {
                pageRequest.setManufacturer(request.getManufacturerPage());
            }
            //0：非以岭  1：以岭
            if (request.getIsYiLing() == 1) {
                pageRequest.setYlFlag(1);
            } else if (request.getIsYiLing() == 2) {
                pageRequest.setYlFlag(0);
            }
            Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage;
            int current = 1;
            do {
                pageRequest.setCurrent(current);
                pageRequest.setSize(500);
                specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
                if (CollUtil.isEmpty(specificationGoodsInfoPage.getRecords())) {
                    break;
                }
                long total = specificationGoodsInfoPage.getTotal();
                if (total > 500L) {
                    throw new BusinessException(UserErrorCode.PLATFORM_GOODS_TO_MANY);
                }
                List<Long> specIdList = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
                List<IntegralOrderPlatformGoodsDO> strategyPlatformGoodsLimitDOList = this.listByRuleIdAndSellSpecificationsIdList(request.getGiveRuleId(), specIdList);
                List<Long> haveSellSpecificationsIdList = strategyPlatformGoodsLimitDOList.stream().map(IntegralOrderPlatformGoodsDO::getSellSpecificationsId).collect(Collectors.toList());
                List<IntegralOrderPlatformGoodsDO> platformGoodsDOList = new ArrayList<>();
                for (StandardSpecificationGoodsInfoBO specificationGoodsInfoBO : specificationGoodsInfoPage.getRecords()) {
                    if (haveSellSpecificationsIdList.contains(specificationGoodsInfoBO.getId())) {
                        continue;
                    }
                    IntegralOrderPlatformGoodsDO platformGoodsDO = new IntegralOrderPlatformGoodsDO();
                    platformGoodsDO.setGiveRuleId(request.getGiveRuleId());
                    platformGoodsDO.setStandardId(specificationGoodsInfoBO.getStandardId());
                    platformGoodsDO.setSellSpecificationsId(specificationGoodsInfoBO.getId());
                    platformGoodsDO.setOpUserId(request.getOpUserId());
                    platformGoodsDO.setOpTime(request.getOpTime());
                    platformGoodsDOList.add(platformGoodsDO);
                }
                if (CollUtil.isNotEmpty(platformGoodsDOList)) {
                    this.saveBatch(platformGoodsDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(specificationGoodsInfoPage.getRecords()));
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(DeleteIntegralGivePlatformGoodsRequest request) {
        IntegralOrderPlatformGoodsDO platformGoodsDO = new IntegralOrderPlatformGoodsDO();
        platformGoodsDO.setOpUserId(request.getOpUserId());

        if (Objects.nonNull(request.getSellSpecificationsId())) {
            LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, request.getGiveRuleId());
            wrapper.eq(IntegralOrderPlatformGoodsDO::getSellSpecificationsId, request.getSellSpecificationsId());
            this.batchDeleteWithFill(platformGoodsDO, wrapper);

        } else if (CollUtil.isNotEmpty(request.getSellSpecificationsIdList())) {
            LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, request.getGiveRuleId());
            wrapper.in(IntegralOrderPlatformGoodsDO::getSellSpecificationsId, request.getSellSpecificationsIdList());
            this.batchDeleteWithFill(platformGoodsDO, wrapper);

        } else {
            StandardSpecificationPageRequest pageRequest = new StandardSpecificationPageRequest();
            if (Objects.nonNull(request.getStandardIdPage())) {
                pageRequest.setStandardId(request.getStandardIdPage());
            }
            if (Objects.nonNull(request.getSellSpecificationsIdPage())) {
                pageRequest.setSpecIdList(ListUtil.toList(request.getSellSpecificationsIdPage()));
            }
            if (StrUtil.isNotEmpty(request.getGoodsNamePage())) {
                pageRequest.setName(request.getGoodsNamePage());
            }
            if (StrUtil.isNotEmpty(request.getManufacturerPage())) {
                pageRequest.setManufacturer(request.getManufacturerPage());
            }
            //0：非以岭  1：以岭
            if (request.getIsYiLing() == 1) {
                pageRequest.setYlFlag(1);
            } else if (request.getIsYiLing() == 2) {
                pageRequest.setYlFlag(0);
            }
            Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage;
            int current = 1;
            do {
                pageRequest.setCurrent(current);
                pageRequest.setSize(500);
                specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
                if (CollUtil.isEmpty(specificationGoodsInfoPage.getRecords())) {
                    break;
                }
                long total = specificationGoodsInfoPage.getTotal();
                if (total > 500L) {
                    throw new BusinessException(UserErrorCode.PLATFORM_GOODS_TO_MANY);
                }
                List<Long> delSellSpecificationsIdList = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
                LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, request.getGiveRuleId());
                wrapper.in(IntegralOrderPlatformGoodsDO::getSellSpecificationsId, delSellSpecificationsIdList);
                this.batchDeleteWithFill(platformGoodsDO, wrapper);
                current = current + 1;
            } while (CollUtil.isNotEmpty(specificationGoodsInfoPage.getRecords()));

        }
        return true;
    }

    @Override
    public void copy(IntegralGiveRuleDO integralGiveRuleDO, Long oldId, Long opUserId) {
        Page<IntegralOrderPlatformGoodsDO> platformGoodsDOPage;
        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, oldId);
        int current = 1;
        do {
            Page<IntegralOrderPlatformGoodsDO> objectPage = new Page<>(current, 100);
            platformGoodsDOPage = this.page(objectPage, wrapper);
            if (CollUtil.isEmpty(platformGoodsDOPage.getRecords())) {
                break;
            }
            List<IntegralOrderPlatformGoodsDO> platformGoodsLimitDOList = platformGoodsDOPage.getRecords();
            for (IntegralOrderPlatformGoodsDO platformGoodsLimitDO : platformGoodsLimitDOList) {
                platformGoodsLimitDO.setId(null);
                platformGoodsLimitDO.setGiveRuleId(integralGiveRuleDO.getId());
                platformGoodsLimitDO.setOpUserId(opUserId);
            }
            if (CollUtil.isNotEmpty(platformGoodsLimitDOList)) {
                this.saveBatch(platformGoodsLimitDOList);
            }
            current = current + 1;
        } while (CollUtil.isNotEmpty(platformGoodsDOPage.getRecords()));
    }

    @Override
    public Page<IntegralOrderPlatformGoodsDO> pageList(QueryIntegralGivePlatformGoodsPageRequest request) {
        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, request.getGiveRuleId());
        if (Objects.nonNull(request.getStandardId())) {
            wrapper.eq(IntegralOrderPlatformGoodsDO::getStandardId, request.getStandardId());
        }
        if (Objects.nonNull(request.getSellSpecificationsId())) {
            wrapper.eq(IntegralOrderPlatformGoodsDO::getSellSpecificationsId, request.getSellSpecificationsId());
        }
        List<IntegralOrderPlatformGoodsDO> platformGoodsDOList = this.list(wrapper);

        List<Long> specIdList = platformGoodsDOList.stream().map(IntegralOrderPlatformGoodsDO::getSellSpecificationsId).collect(Collectors.toList());
        if (CollUtil.isEmpty(specIdList)) {
            return request.getPage();
        }
        StandardSpecificationPageRequest pageRequest = PojoUtils.map(request, StandardSpecificationPageRequest.class);
        pageRequest.setSpecIdList(specIdList);
        pageRequest.setName(request.getGoodsName());
        //0：非以岭  1：以岭
        if (request.getIsYiLing() == 1) {
            pageRequest.setYlFlag(1);
        } else if (request.getIsYiLing() == 2) {
            pageRequest.setYlFlag(0);
        }
        Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);

        Page<IntegralOrderPlatformGoodsDO> doPage = PojoUtils.map(specificationGoodsInfoPage, IntegralOrderPlatformGoodsDO.class);
        if (CollUtil.isEmpty(doPage.getRecords())) {
            return request.getPage();
        }
        List<Long> sellSpecificationsIdPage = specificationGoodsInfoPage.getRecords().stream().map(StandardSpecificationGoodsInfoBO::getId).collect(Collectors.toList());
        List<IntegralOrderPlatformGoodsDO> strategyPlatformGoodsLimitDOList = this.listByRuleIdAndSellSpecificationsIdList(request.getGiveRuleId(), sellSpecificationsIdPage);
        doPage.setRecords(strategyPlatformGoodsLimitDOList);
        return doPage;
    }

    @Override
    public Integer countPlatformGoodsByGiveRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, giveRuleId);
        return this.count(wrapper);
    }

    @Override
    public List<IntegralOrderPlatformGoodsDO> listPlatformGoodsByGiveRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, giveRuleId);
        return this.list(wrapper);
    }

    @Override
    public IntegralOrderPlatformGoodsDO queryByRuleIdAndSellSpecificationsId(Long giveRuleId, Long sellSpecificationsId) {
        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, giveRuleId);
        wrapper.eq(IntegralOrderPlatformGoodsDO::getSellSpecificationsId, sellSpecificationsId);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<IntegralOrderPlatformGoodsDO> listByRuleIdAndSellSpecificationsIdList(Long giveRuleId, List<Long> sellSpecificationsIdList) {
        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, giveRuleId);
        if (CollUtil.isNotEmpty(sellSpecificationsIdList)) {
            wrapper.in(IntegralOrderPlatformGoodsDO::getSellSpecificationsId, sellSpecificationsIdList);
        }
        return this.list(wrapper);
    }

    @Override
    public List<IntegralOrderPlatformGoodsDO> listByRuleIdAndStandardIdList(Long giveRuleId, List<Long> standardIdList) {
        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderPlatformGoodsDO::getGiveRuleId, giveRuleId);
        if (CollUtil.isNotEmpty(standardIdList)) {
            wrapper.in(IntegralOrderPlatformGoodsDO::getStandardId, standardIdList);
        }
        return this.list(wrapper);
    }

}
