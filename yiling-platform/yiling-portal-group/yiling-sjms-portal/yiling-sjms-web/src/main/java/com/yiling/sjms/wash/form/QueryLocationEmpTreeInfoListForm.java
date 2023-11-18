package com.yiling.sjms.wash.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLocationEmpTreeInfoListForm extends BaseForm {

    /**
     * 区域备案id，如果当前在编辑页面，则必传
     */
    @ApiModelProperty(value = "区域备案id，如果当前在编辑页面，则必传")
    private Long unlockAreaRecordId;

    /**
     * 非锁客户分类
     */
    @NotNull(message = "非锁客户分类不可为空")
    @ApiModelProperty(value = "非锁客户分类")
    private Integer customerClassification;

    /**
     * 品种id
     */
    @NotNull(message = "品种不可为空")
    @ApiModelProperty(value = "品种id")
    private Long categoryId;

}
