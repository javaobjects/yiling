package com.yiling.sjms.flee.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
public class QueryFleeingGoodsFormListForm extends QueryPageListForm {

    /**
     * form表主键
     */
    @ApiModelProperty(value = "form表主键")
    private Long formId;

    /**
     * 文件类型 1-申报 2-申报确认
     */
    @ApiModelProperty(value = "文件类型 1-申报 2-申报确认")
    private Integer importFileType;
}
