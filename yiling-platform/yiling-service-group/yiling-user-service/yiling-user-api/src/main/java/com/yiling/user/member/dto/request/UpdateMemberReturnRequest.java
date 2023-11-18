package com.yiling.user.member.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员确认退款 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMemberReturnRequest extends BaseRequest {

    /**
     * 购买记录ID
     */
    @NotNull
    private Long id;

    /**
     * 提交退款金额
     */
    @NotNull
    @DecimalMin(value = "0.01",message = "退款金额不能小于0")
    private BigDecimal submitReturnAmount;

    /**
     * 退款原因
     */
    @NotEmpty
    private String returnReason;

    /**
     * 退款备注
     */
    private String returnRemark;
}
