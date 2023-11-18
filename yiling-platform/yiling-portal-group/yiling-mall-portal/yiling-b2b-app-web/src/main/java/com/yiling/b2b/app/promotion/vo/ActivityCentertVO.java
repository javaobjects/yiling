package com.yiling.b2b.app.promotion.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shixing.sun
 * @date 2022-05-06
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class ActivityCentertVO {
    @ApiModelProperty(value = "满赠")
    private List<String> giftLimit;

    @ApiModelProperty(value = "组合包")
    private List<String> combination;

    @ApiModelProperty(value = "秒杀")
    private List<String> seckill;

    @ApiModelProperty(value = "特价")
    private List<String> specialPrice;

    @ApiModelProperty(value = "满赠活动数量")
    private Integer giftLimitNumber;

    @ApiModelProperty(value = "组合包活动数量")
    private Integer combinationNumber;

    @ApiModelProperty(value = "满赠活秒杀活动数量动数量")
    private Integer seckillNumber;

    @ApiModelProperty(value = "特价活动数量")
    private Integer specialPriceNumber;
}
