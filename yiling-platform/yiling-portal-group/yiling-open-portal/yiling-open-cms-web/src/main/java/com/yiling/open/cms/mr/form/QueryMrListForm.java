package com.yiling.open.cms.mr.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询医药代表信息列表 Form
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
public class QueryMrListForm {

    @NotEmpty
    @ApiModelProperty(value = "医药代表ID列表", required = true)
    private List<Long> ids;
}
