package com.yiling.admin.b2b.member.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-更新会员推广人或推广方 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class UpdateMemberPromoterForm extends BaseForm {

    /**
     * 购买记录ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "购买记录ID", required = true)
    private Long id;

    /**
     * 推广方ID
     */
    @ApiModelProperty("推广方ID")
    private Long promoterId;

    /**
     * 推广人手机号
     */
    @Length(max = 50)
    @ApiModelProperty("推广人手机号")
    private String mobile;

}
