package com.yiling.admin.data.center.enterprise.form;

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
 * 查询企业审核分页列表 Form
 *
 * @author: lun.yu
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseAuthForm extends BaseForm {

    /**
     * 企业ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("企业ID")
    private Long id;

    /**
     * 认证状态：0-全部 1-未认证 2-认证通过 3-认证不通过
     */
    @NotNull
    @Range(min = 2,max = 3)
    @ApiModelProperty("审核状态： 2-审核通过 3-审核不通过")
    private Integer authStatus;

    /**
     * 审核驳回原因
     */
    @Length(max = 200)
    @ApiModelProperty("审核驳回原因")
    private String authRejectReason;


}
