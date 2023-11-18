package com.yiling.marketing.strategy.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;

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
public class SaveActivityAutoGiveGiftRequest extends BaseRequest {
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
     * 执行开始时间
     */
    private Date startTime;

    /**
     * 赠品信息表
     */
    private List<StrategyGiftDTO> strategyGiftList;
}
