package com.yiling.sjms.wash.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/3/7
 */
@Data
public class QueryFlowMonthWashControlPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

}
