package com.yiling.sjms.manor.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 医院辖区变更表单
 * </p>
 *
 * @author gxl
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ManorChangeItemForm extends BaseForm {



    @ApiModelProperty(value = "辖区变更明细id(修改时传)")
    private Long id;

    /**
     * 品种id
     */
    @ApiModelProperty(value = "品种id")
    private Long categoryId;

    /**
     * 旧辖区id
     */
    @ApiModelProperty(value = "旧辖区id")
    private Long manorId;

    /**
     * 新辖区id
     */
    @ApiModelProperty(value = "新辖区id")
    private Long newManorId;




}
