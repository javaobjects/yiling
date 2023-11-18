package com.yiling.user.integral.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.dao.IntegralOrderPlatformGoodsMapper;
import com.yiling.user.integral.dto.IntegralOrderPlatformGoodsDTO;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderPlatformGoodsDO;
import com.yiling.user.integral.service.IntegralOrderPlatformGoodsService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
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
    public Map<Long, List<IntegralOrderPlatformGoodsDTO>> getMapPlatformGoodsByGiveRuleIdList(List<Long> giveRuleIdList) {
        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IntegralOrderPlatformGoodsDO::getGiveRuleId, giveRuleIdList);
        List<IntegralOrderPlatformGoodsDTO> platformGoodsDTOList = PojoUtils.map(this.list(wrapper), IntegralOrderPlatformGoodsDTO.class);
        return platformGoodsDTOList.stream().collect(Collectors.groupingBy(IntegralOrderPlatformGoodsDTO::getGiveRuleId));
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

    @Override
    public List<IntegralOrderPlatformGoodsDO> listByRuleIdListAndSpecificationsIdList(List<Long> giveRuleIdList, List<Long> sellSpecificationsIdList) {
        if (CollUtil.isEmpty(giveRuleIdList)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<IntegralOrderPlatformGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IntegralOrderPlatformGoodsDO::getGiveRuleId, giveRuleIdList);
        if (CollUtil.isNotEmpty(sellSpecificationsIdList)) {
            wrapper.in(IntegralOrderPlatformGoodsDO::getSellSpecificationsId, sellSpecificationsIdList);
        }
        return this.list(wrapper);
    }

}
