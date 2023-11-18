package com.yiling.admin.b2b.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单送积分-指定会员方案 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralGiveMemberVO extends BaseVO {

    /**
     * 发放规则ID
     */
    @ApiModelProperty("发放规则ID")
    private Long giveRuleId;

    /**
     * 会员ID
     */
    @ApiModelProperty("会员ID")
    private Long memberId;

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String memberName;

}
