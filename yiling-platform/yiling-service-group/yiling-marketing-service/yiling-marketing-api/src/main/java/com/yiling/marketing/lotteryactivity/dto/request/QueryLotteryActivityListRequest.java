package com.yiling.marketing.lotteryactivity.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询抽奖活动列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-15
 */
@Data
@Accessors(chain = true)
public class QueryLotteryActivityListRequest extends BaseRequest {

    /**
     * 活动ID
     */
    @Eq
    private Long id;

    /**
     * 活动名称
     */
    @Eq
    private String activityName;

    /**
     * 活动状态：1-启用 2-停用
     */
    @Eq
    private Integer status;

    /**
     * 活动进度：1-未开始 2-进行中 3-已结束 （动态状态，枚举：lottery_activity_progress ，字典：LotteryActivityProgressEnums）
     */
    private Integer progress;

}
