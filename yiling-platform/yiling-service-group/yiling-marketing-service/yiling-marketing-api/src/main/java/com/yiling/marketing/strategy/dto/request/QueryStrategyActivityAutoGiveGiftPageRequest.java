package com.yiling.marketing.strategy.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/9/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyActivityAutoGiveGiftPageRequest extends QueryPageListRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 续费的订单id
     */
    private Long orderId;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 发放时间 格式yyyy-MM-dd
     */
    private String giveTime;

    /**
     * 赠品类型(1-商品优惠券；2-会员优惠券；3-抽奖次数)
     */
    private List<Integer> typeList;

    /**
     * 赠品id
     */
    private Long giftId;

    /**
     * 限制时间
     */
    private Date startTime;
}
