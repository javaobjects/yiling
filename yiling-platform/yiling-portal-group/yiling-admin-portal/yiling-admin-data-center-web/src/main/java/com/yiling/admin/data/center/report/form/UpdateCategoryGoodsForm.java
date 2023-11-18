package com.yiling.admin.data.center.report.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCategoryGoodsForm extends BaseForm {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty("id")
    private Long id;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty("结束时间")
    private Date endTime;
}