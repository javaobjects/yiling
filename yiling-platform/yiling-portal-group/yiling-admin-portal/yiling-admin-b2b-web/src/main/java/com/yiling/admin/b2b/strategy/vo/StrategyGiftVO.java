package com.yiling.admin.b2b.strategy.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动赠品表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyGiftVO extends BaseVO {

    @ApiModelProperty("营销活动id")
    private Long marketingStrategyId;

    @ApiModelProperty("活动阶梯id")
    private Long ladderId;

    @ApiModelProperty("赠品类型(1-商品优惠券；2-会员优惠券；3-抽奖次数)")
    private Integer type;

    @ApiModelProperty("赠品id")
    private Long giftId;

    @ApiModelProperty("数量")
    private Integer count;

    // =======================================

    @ApiModelProperty("赠品名称")
    private String giftName;
}
