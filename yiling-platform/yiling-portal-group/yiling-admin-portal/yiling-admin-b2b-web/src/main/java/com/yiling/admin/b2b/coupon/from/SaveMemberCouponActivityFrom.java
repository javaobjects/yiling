package com.yiling.admin.b2b.coupon.from;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@Accessors(chain = true)
public class SaveMemberCouponActivityFrom {

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "优惠券id")
    private Long id;

    /**
     * 会员规格id数组
     */
    @ApiModelProperty(value = "会员规格id数组（列表id非memberId）/当删除的时候表示列表id数组")
    private List<Long> ids;
}
