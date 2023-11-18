package com.yiling.b2b.app.lotteryactivity.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动-中奖信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-05
 */
@Data
@Accessors(chain = true)
public class LotteryActivityRuleVO extends BaseVO {

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 活动规则说明
     */
    @ApiModelProperty("活动规则说明")
    private String content;


}
