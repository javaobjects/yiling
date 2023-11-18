package com.yiling.marketing.promotion.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.promotion.dao.PromotionSecKillSpecialMapper;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.entity.PromotionSecKillSpecialDO;
import com.yiling.marketing.promotion.service.PromotionSecKillSpecialService;

/**
 * <p>
 * 促销活动秒杀特价服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-1-14
 */
@Service
public class PromotionSecKillSpecialServiceImpl extends BaseServiceImpl<PromotionSecKillSpecialMapper, PromotionSecKillSpecialDO>
                                                implements PromotionSecKillSpecialService {

    @Override
    public PromotionSecKillSpecialDO queryByPromotionActivityId(Long promotionActivityId) {
        QueryWrapper<PromotionSecKillSpecialDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PromotionSecKillSpecialDO::getPromotionActivityId, promotionActivityId);
        return this.getOne(wrapper);
    }

    @Override
    public List<PromotionSecKillSpecialDO> queryByPromotionActivityIdList(List<Long> promotionActivityIdList) {
        QueryWrapper<PromotionSecKillSpecialDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionSecKillSpecialDO::getPromotionActivityId, promotionActivityIdList);
        return this.list(wrapper);
    }

    @Override
    public void copy(PromotionActivityStatusRequest request, Long promotionActivityId) {
        PromotionSecKillSpecialDO secKillSpecial = this.queryByPromotionActivityId(request.getId());
        if (Objects.isNull(secKillSpecial)) {
            return;
        }
        PromotionSecKillSpecialDO newSecKillSpecialDO = PromotionSecKillSpecialDO.builder().promotionActivityId(promotionActivityId)
            .terminalType(secKillSpecial.getTerminalType()).permittedAreaType(secKillSpecial.getPermittedAreaType())
            .permittedAreaDetail(secKillSpecial.getPermittedAreaDetail()).permittedEnterpriseType(secKillSpecial.getPermittedEnterpriseType())
            .permittedEnterpriseDetail(secKillSpecial.getPermittedEnterpriseDetail()).createUser(request.getOpUserId())
            .createTime(request.getOpTime()).updateUser(request.getOpUserId()).updateTime(request.getOpTime()).build();
        this.save(newSecKillSpecialDO);
    }
}
