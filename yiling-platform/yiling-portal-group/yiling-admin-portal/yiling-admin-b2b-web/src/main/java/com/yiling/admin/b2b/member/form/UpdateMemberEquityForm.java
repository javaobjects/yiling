package com.yiling.admin.b2b.member.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-修改权益 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMemberEquityForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 权益名称
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty("权益名称")
    private String name;

    /**
     * 权益图标
     */
    @NotEmpty
    @ApiModelProperty("权益图标")
    private String icon;

    /**
     * 权益说明
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty("权益说明")
    private String description;

    /**
     * 权益状态：0-关闭 1-开启
     */
    @Range(min = 0,max = 1)
    @NotNull
    @ApiModelProperty("权益状态：0-关闭 1-开启")
    private Integer status;


}
