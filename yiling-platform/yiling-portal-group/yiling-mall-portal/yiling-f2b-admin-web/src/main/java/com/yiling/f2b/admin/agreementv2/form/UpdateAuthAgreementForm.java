package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议审核 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateAuthAgreementForm extends BaseForm {

    /**
     * 协议ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "协议ID", required = true)
    private Long id;

    /**
     * 纸质件编号
     */
    @Length(max = 100)
    @ApiModelProperty("纸质件编号")
    private String paperNo;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    @NotNull
    @Range(min = 1, max = 4)
    @ApiModelProperty(value = "审核状态：1-待审核 2-审核通过 3-审核驳回 4-已归档", required = true)
    private Integer authStatus;

    /**
     * 审核拒绝原因
     */
    @Length(max = 2000)
    @ApiModelProperty("审核拒绝原因（审核驳回时必须传入值）")
    private String authRejectReason;

}
