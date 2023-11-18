package com.yiling.b2b.app.goods.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyActivityVO extends BaseVO {

    /**
     * 活动类型
     */
    @ApiModelProperty("活动类型 1-策略满赠；")
    private Integer type;

    @ApiModelProperty("活动标题")
    private String title;

    /**
     * 优惠规则
     */
    @ApiModelProperty("赠品规则")
    private String giftRules;

    @ApiModelProperty("开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;
}
