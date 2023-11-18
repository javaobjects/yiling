package com.yiling.admin.b2b.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单送积分-指定客户 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralGiveEnterpriseVO extends BaseVO {

    @ApiModelProperty("发放规则ID")
    private Long giveRuleId;

    @ApiModelProperty("企业ID")
    private Long eid;

    @ApiModelProperty("企业名称")
    private String ename;

}
