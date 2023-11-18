package com.yiling.marketing.couponactivity.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.couponactivity.dao.CouponActivityEnterpriseGetRulesMapper;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseGetRulesDO;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseGetRulesService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 商家券自主领券规则表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-11-10
 */
@Service
public class CouponActivityEnterpriseGetRulesServiceImpl extends
                                                         BaseServiceImpl<CouponActivityEnterpriseGetRulesMapper, CouponActivityEnterpriseGetRulesDO>
                                                         implements CouponActivityEnterpriseGetRulesService {

    @Override
    public CouponActivityEnterpriseGetRulesDO getByCouponActivityId(Long couponActivityId) {
        if (ObjectUtil.isNull(couponActivityId)) {
            return null;
        }
        LambdaQueryWrapper<CouponActivityEnterpriseGetRulesDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CouponActivityEnterpriseGetRulesDO::getCouponActivityId, couponActivityId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<CouponActivityEnterpriseGetRulesDO> getByCouponActivityIdList(List<Long> couponActivityIdList) {
        if (CollUtil.isEmpty(couponActivityIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityEnterpriseGetRulesDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CouponActivityEnterpriseGetRulesDO::getCouponActivityId, couponActivityIdList);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public Integer deleteById(CouponActivityEnterpriseGetRulesDO enterpriseGetRules) {
        if (ObjectUtil.isNull(enterpriseGetRules) || ObjectUtil.isNull(enterpriseGetRules.getId())
            || ObjectUtil.isNull(enterpriseGetRules.getOpUserId())) {
            return 0;
        }
        return this.deleteByIdWithFill(enterpriseGetRules);
    }
}
