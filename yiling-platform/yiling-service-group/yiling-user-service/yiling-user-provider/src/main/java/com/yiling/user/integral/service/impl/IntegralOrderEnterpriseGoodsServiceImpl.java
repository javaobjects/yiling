package com.yiling.user.integral.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.dao.IntegralOrderEnterpriseGoodsMapper;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderEnterpriseGoodsDO;
import com.yiling.user.integral.service.IntegralOrderEnterpriseGoodsService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单送积分店铺SKU表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralOrderEnterpriseGoodsServiceImpl extends BaseServiceImpl<IntegralOrderEnterpriseGoodsMapper, IntegralOrderEnterpriseGoodsDO> implements IntegralOrderEnterpriseGoodsService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copy(IntegralGiveRuleDO giveRuleDO, Long oldId, Long opUserId) {
        Page<IntegralOrderEnterpriseGoodsDO> enterpriseGoodsDOPage;
        QueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, oldId);
        int current = 1;

        do {
            Page<IntegralOrderEnterpriseGoodsDO> objectPage = new Page<>(current, 100);
            enterpriseGoodsDOPage = this.page(objectPage, wrapper);
            if (CollUtil.isEmpty(enterpriseGoodsDOPage.getRecords())) {
                break;
            }
            List<IntegralOrderEnterpriseGoodsDO> enterpriseGoodsLimitDOList = enterpriseGoodsDOPage.getRecords();
            for (IntegralOrderEnterpriseGoodsDO enterpriseGoodsLimitDO : enterpriseGoodsLimitDOList) {
                enterpriseGoodsLimitDO.setId(null);
                enterpriseGoodsLimitDO.setGiveRuleId(giveRuleDO.getId());
                enterpriseGoodsLimitDO.setOpUserId(opUserId);
            }
            if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                this.saveBatch(enterpriseGoodsLimitDOList);
            }
            current = current + 1;
            
        } while (CollUtil.isNotEmpty(enterpriseGoodsDOPage.getRecords()));
    }

    @Override
    public Integer countEnterpriseGoodsByRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleId);
        return this.count(wrapper);
    }

    @Override
    public List<IntegralOrderEnterpriseGoodsDO> listEnterpriseGoodsByRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleId);
        return this.list(wrapper);
    }

    @Override
    public IntegralOrderEnterpriseGoodsDO queryByRuleIdAndGoodsId(Long giveRuleId, Long goodsId) {
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleId);
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGoodsId, goodsId);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<IntegralOrderEnterpriseGoodsDO> listByRuleIdAndGoodsIdList(Long giveRuleId, List<Long> goodsIdList) {
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleId);
        if (CollUtil.isNotEmpty(goodsIdList)) {
            wrapper.in(IntegralOrderEnterpriseGoodsDO::getGoodsId, goodsIdList);
        }
        return this.list(wrapper);
    }

    @Override
    public List<IntegralOrderEnterpriseGoodsDO> listEnterpriseGoodsByRuleIdList(List<Long> giveRuleIdList) {
        if (CollUtil.isEmpty(giveRuleIdList)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleIdList);
        return this.list(wrapper);
    }

}
