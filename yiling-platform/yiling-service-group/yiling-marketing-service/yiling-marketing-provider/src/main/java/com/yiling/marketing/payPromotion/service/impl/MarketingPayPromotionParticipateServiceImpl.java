package com.yiling.marketing.payPromotion.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.dao.MarketingPayPromotionParticipateMapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionParticipateDO;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionParticipateService;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRecordRequest;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 支付促销参与记录表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-04-19
 */
@Service
public class MarketingPayPromotionParticipateServiceImpl extends BaseServiceImpl<MarketingPayPromotionParticipateMapper, MarketingPayPromotionParticipateDO> implements MarketingPayPromotionParticipateService {

    @Override
    public Integer countRecordByActivityIdAndEid(Long activityId, Long eid) {
        QueryWrapper<MarketingPayPromotionParticipateDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromotionParticipateDO::getMarketingPayId, activityId);
        wrapper.lambda().eq(MarketingPayPromotionParticipateDO::getEid, eid);
        return this.count(wrapper);
    }

    @Override
    public boolean savePayPromotionRecord(List<SavePayPromotionRecordRequest> requestList) {
        List<MarketingPayPromotionParticipateDO> shopRecord = new ArrayList<>();
        for (SavePayPromotionRecordRequest request : requestList) {
            MarketingPayPromotionParticipateDO promotionParticipateDO = PojoUtils.map(request, MarketingPayPromotionParticipateDO.class);
            promotionParticipateDO.setParticipateTime(new Date());
            if (Objects.nonNull(request.getShopActivityId())) {
                promotionParticipateDO.setMarketingPayId(request.getShopActivityId());
                promotionParticipateDO.setRuleId(request.getShopRuleId());
                shopRecord.add(promotionParticipateDO);
            }
            if (Objects.nonNull(request.getPlatformActivityId())) {
                promotionParticipateDO.setMarketingPayId(request.getPlatformActivityId());
                promotionParticipateDO.setRuleId(request.getPlatformRuleId());
                shopRecord.add(promotionParticipateDO);
            }
        }
        if (CollUtil.isNotEmpty(shopRecord)) {
            this.saveBatch(shopRecord);
        }
        return true;
    }

    @Override
    public Integer countRecordByActivityId(Long activityId) {
        QueryWrapper<MarketingPayPromotionParticipateDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromotionParticipateDO::getMarketingPayId, activityId);
        return this.count(wrapper);
    }
}
