package com.yiling.admin.b2b.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单送积分-商家VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralGiveSellerVO extends BaseVO {

    /**
     * 发放规则ID
     */
    @ApiModelProperty("发放规则ID")
    private Long giveRuleId;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;
}
