package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议归档 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateArchiveAgreementForm extends BaseForm {

    /**
     * 协议ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "协议ID", required = true)
    private Long id;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    @NotEmpty
    @Length(max = 10)
    @ApiModelProperty(value = "归档编号", required = true)
    private String archiveNo;

    /**
     * 审核拒绝原因
     */
    @NotEmpty
    @Length(max = 200)
    @ApiModelProperty(value = "归档备注", required = true)
    private String archiveRemark;

}
