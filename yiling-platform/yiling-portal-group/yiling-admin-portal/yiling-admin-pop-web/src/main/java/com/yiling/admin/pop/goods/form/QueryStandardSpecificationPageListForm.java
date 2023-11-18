package com.yiling.admin.pop.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStandardSpecificationPageListForm extends QueryPageListForm {
    /**
     * 审核ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 标准库id
     */
    @ApiModelProperty(value = "标准库id")
    private Long standardId;
}
