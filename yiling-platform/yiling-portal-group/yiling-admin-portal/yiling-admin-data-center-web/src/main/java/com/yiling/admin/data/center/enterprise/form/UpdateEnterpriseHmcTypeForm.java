package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新企业HMC业务类型 Form
 *
 * @author: xuan.zhou
 * @date: 2022/4/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseHmcTypeForm extends BaseForm {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "企业ID", required = true)
    private Long eid;

    @NotNull
    @Range(min = 1, max = 3)
    @ApiModelProperty(value = "HMC业务类型：1-药+险销售 2-药+险销售与药品兑付 3-医药代表", required = true)
    private Integer hmcType;
}
