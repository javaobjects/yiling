package com.yiling.admin.b2b.integral.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分发放/扣减记录 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralGiveUseRecordVO extends BaseVO {

    /**
     * 规则ID
     */
    @ApiModelProperty("规则ID")
    private Long ruleId;

    /**
     * 规则名称
     */
    @ApiModelProperty("规则名称")
    private String ruleName;

    /**
     * 积分值
     */
    @ApiModelProperty("积分值")
    private Integer integralValue;

    /**
     * 发放/扣减时间
     */
    @ApiModelProperty("发放/扣减时间")
    private Date operTime;

    /**
     * 行为ID
     */
    @ApiModelProperty("行为ID")
    private Long behaviorId;

    /**
     * 行为名称
     */
    @ApiModelProperty("行为名称")
    private String behaviorName;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long uid;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String uname;

    /**
     * 规则说明
     */
    @ApiModelProperty("规则说明")
    private String description;

    /**
     * 提交人手机号
     */
    @ApiModelProperty("提交人手机号")
    private String mobile;

    /**
     * 操作备注
     */
    @ApiModelProperty("操作备注")
    private String opRemark;

}
