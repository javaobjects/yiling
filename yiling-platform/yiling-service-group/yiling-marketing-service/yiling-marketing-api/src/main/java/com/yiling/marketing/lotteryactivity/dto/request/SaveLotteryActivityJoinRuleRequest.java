package com.yiling.marketing.lotteryactivity.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存抽奖活动-C端参与规则 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveLotteryActivityJoinRuleRequest extends BaseRequest {

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 活动参与门槛：1-不限制 2-关注健康管理中心公众号
     */
    private Integer joinStage;

    /**
     * 活动期间每天赠送抽奖次数
     */
    private Integer everyGive;

    /**
     * 活动签到赠送抽奖次数
     */
    private Integer signGive;

    /**
     * 活动邀请新粉丝赠送抽奖次数
     */
    private Integer inviteGive;

}
