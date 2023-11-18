package com.yiling.admin.b2b.coupon.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityMemberPageVo extends BaseVO {

    /**
     * 会员ID
     */
    @ApiModelProperty("会员ID,提交的时候提交id（会员id）")
    private Long memberId;

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 有效时长
     */
    @ApiModelProperty("有效时长")
    private Integer validTime;

    /**
     * 名称（如：季卡VIP）
     */
    @ApiModelProperty("名称（如：季卡VIP）")
    private String name;

    /**
     * 排序
     */
    private Integer sort;
}
