package com.yiling.admin.b2b.member.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-保存会员权益关系Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-11-28
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveMemberEquityRelationForm extends BaseForm {

    /**
     * 权益ID
     */
    @NotNull
    @ApiModelProperty(value = "权益ID", required = true)
    private Long equityId;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;


}
