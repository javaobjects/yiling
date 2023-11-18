package com.yiling.admin.b2b.member.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-更新会员排序 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class UpdateMemberSortForm extends BaseForm {

    /**
     * 会员ID
     */
    @NotNull
    @ApiModelProperty(value = "会员ID", required = true)
    private Long id;

    /**
     * 排序
     */
    @NotNull
    @Range(min = 0, max = 200)
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

}
