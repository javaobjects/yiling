package com.yiling.admin.b2b.common.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppHotWordsWeightForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;
    
    @ApiModelProperty(value = "排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    private Integer sort;
}
