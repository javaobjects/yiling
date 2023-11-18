package com.yiling.admin.sales.assistant.task.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: ray
 * @date: 2021/12/20
 */
@Data
@ApiModel
public class MemberPromotionVO {
    @ApiModelProperty(value = "活动id")
    private Long promotionActivityId;

    @ApiModelProperty(value = "赠品信息表id")
    private Long goodsGiftId;

    private String name;

    private Date beginTime;

    private Date endTime;
    @ApiModelProperty(value = "条件")
    private BigDecimal promotionAmount;
    @ApiModelProperty(value = "会员名称")
    private String giftName;
}