package com.yiling.admin.b2b.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户签到详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSignDetailVO extends BaseVO {

    /**
     * 发放规则ID
     */
    @ApiModelProperty("发放规则ID")
    private Long giveRuleId;

    /**
     * 发放规则名称
     */
    @ApiModelProperty("发放规则名称")
    private String giveRuleName;

    /**
     * 发放积分
     */
    @ApiModelProperty("发放积分")
    private Integer integralValue;

}
