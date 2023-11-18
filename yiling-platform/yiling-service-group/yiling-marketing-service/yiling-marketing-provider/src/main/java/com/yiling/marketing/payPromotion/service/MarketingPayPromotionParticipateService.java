package com.yiling.marketing.payPromotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionParticipateDO;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRecordRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;

/**
 * <p>
 * 支付促销参与记录表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-04-19
 */
public interface MarketingPayPromotionParticipateService extends BaseService<MarketingPayPromotionParticipateDO> {

    /**
     * 查看某用户某活动的参与次数
     *
     * @param activityId 活动id
     * @param eid 客户id
     * @return 活动参与次数
     */
    Integer countRecordByActivityIdAndEid(Long activityId, Long eid);

    /**
     * 新增支付促销活动参与记录
     *
     * @param requestList 新增数据
     * @return 成功/失败
     */
    boolean savePayPromotionRecord(List<SavePayPromotionRecordRequest> requestList);

    /**
     * 查看某用户某活动的参与次数
     *
     * @param activityId 活动id
     * @return 活动参与次数
     */
    Integer countRecordByActivityId(Long activityId);
}
