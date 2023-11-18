package com.yiling.marketing.payPromotion.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionActivityDO;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.request.QueryPayPromotionActivityPageRequest;

/**
 * <p>
 * 支付促销主表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Repository
public interface MarketingPayPromotionActivityMapper extends BaseMapper<MarketingPayPromotionActivityDO> {

    Page<PayPromotionActivityDTO> pageList(Page<MarketingPayPromotionActivityDO> page, @Param("request") QueryPayPromotionActivityPageRequest request);

    List<MarketingPayPromotionActivityDO> listEffectiveActivity(@Param("sponsorType") Integer sponsorType, @Param("time") Date time, @Param("eidList") List<Long> eidList);

    int addRecordTimes(@Param("activityId") Long activityId, @Param("times") int times, @Param("opUserId") Long opUserId, @Param("opTime") Date opTime);
}
