package com.yiling.user.integral.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 生成订单送积分倍数配置 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GenerateMultipleConfigDTO extends BaseDTO {

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 客户范围：1-全部客户 2-指定客户 3-指定客户范围
     */
    private Integer customerScope;

    /**
     * 指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员（部分会员具体到会员方案上）
     */
    private Integer userType;

    /**
     * 用户类型名称（客户范围为1-全部客户 2-指定客户时展示客户范围名称；客户范围为3-指定客户范围时展示用户类型名称；用户类型为部分会员时展示会员名称）
     */
    private String userTypeName;

    /***
     * 部分会员时的会员ID
     */
    private Long memberId;

    /**
     * 支付方式：0-全部支付方式 1-线上支付 2-线下支付 3-账期支付
     */
    private Integer paymentMethod;

    /**
     * 积分倍数
     */
    private BigDecimal integralMultiple;

}
