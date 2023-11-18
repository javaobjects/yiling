package com.yiling.admin.b2b.strategy.form;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/8/26
 */
@Data
public class SaveStrategyGiftForm implements Serializable {
    
    @ApiModelProperty("赠品表id")
    private Long id;

    @ApiModelProperty("营销活动id")
    private Long marketingStrategyId;

    @ApiModelProperty("活动阶梯id")
    private Long ladderId;

    @ApiModelProperty("赠品类型(1-商品优惠券；2-会员优惠券；3-抽奖次数)")
    private Integer type;

    @ApiModelProperty("赠品id")
    private Long giftId;

    @ApiModelProperty("数量")
    private Long count;
}
