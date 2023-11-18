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
public class SimpleActivityVO  extends BaseVO {

    /**
     * 优惠券名称
     */
    @ApiModelProperty("优惠券名称")
    private String name;

    /**
     * 活动类型
     */
    @ApiModelProperty("活动类型 1-满赠；2-特价；3-秒杀")
    private Integer type;

    /**
     * 优惠规则
     */
    @ApiModelProperty("优惠规则")
    private String couponRules;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;
}
