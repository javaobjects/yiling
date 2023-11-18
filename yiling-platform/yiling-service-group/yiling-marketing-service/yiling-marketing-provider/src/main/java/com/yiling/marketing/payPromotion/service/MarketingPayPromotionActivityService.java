package com.yiling.marketing.payPromotion.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionActivityDO;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.request.AddPayPromotionActivityRequest;
import com.yiling.marketing.paypromotion.dto.request.QueryPayPromotionActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;

/**
 * <p>
 * 支付促销主表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
public interface MarketingPayPromotionActivityService extends BaseService<MarketingPayPromotionActivityDO> {

    Page<PayPromotionActivityDTO> pageList(QueryPayPromotionActivityPageRequest request);

    Long saveBasic(AddPayPromotionActivityRequest request);

    List<MarketingPayPromotionActivityDO> listEffectiveActivity(Integer sponsorType, Date time, List<Long> eidList);

    int addRecordTimes(Long activityId, int times, Long opUserId, Date opTime);

    /**
     * 支付促销的复制
     *
     * @param request 活动id
     * @return 满赠促销活动主信息
     */
    PayPromotionActivityDTO copy(CopyStrategyRequest request);
}
