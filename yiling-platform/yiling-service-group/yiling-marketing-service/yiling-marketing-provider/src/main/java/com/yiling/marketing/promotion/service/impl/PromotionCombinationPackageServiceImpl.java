package com.yiling.marketing.promotion.service.impl;

import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.marketing.promotion.dao.PromotionCombinationPackageMapper;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.entity.PromotionCombinationPackageDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.promotion.service.PromotionCombinationPackageService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 促销活动组合包表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-04-27
 */
@Service
public class PromotionCombinationPackageServiceImpl extends BaseServiceImpl<PromotionCombinationPackageMapper, PromotionCombinationPackageDO> implements PromotionCombinationPackageService {

    @Override
    public PromotionCombinationPackageDO queryByPromotionActivityId(Long promotionActivityId) {
        QueryWrapper<PromotionCombinationPackageDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PromotionCombinationPackageDO::getPromotionActivityId, promotionActivityId);
        return this.getOne(wrapper);
    }

    @Override
    public void copy(PromotionActivityStatusRequest request, Long promotionActivityId) {
        PromotionCombinationPackageDO combinationPackage = this.queryByPromotionActivityId(request.getId());
        if (Objects.isNull(combinationPackage)) {
            return;
        }
        PromotionCombinationPackageDO newCombinationPackage = new PromotionCombinationPackageDO().
                setPromotionActivityId(promotionActivityId).setPackageName(combinationPackage.getPackageName()).setInitialNum(combinationPackage.getInitialNum()).setTotalNum(combinationPackage.getTotalNum()).setPerDayNum(combinationPackage.getPerDayNum()).setPerPersonNum(combinationPackage.getPerPersonNum()).setReturnRequirement(combinationPackage.getReturnRequirement()).setPackageShortName(combinationPackage.getPackageShortName()).setPic(combinationPackage.getPic()).setRemark(combinationPackage.getRemark()).setCreateUser(request.getOpUserId()).setDescriptionOfOtherActivity(combinationPackage.getDescriptionOfOtherActivity()).setCreateTime(request.getOpTime()).setUpdateUser(request.getOpUserId()).setUpdateTime(request.getOpTime());
        this.save(newCombinationPackage);
    }
}
