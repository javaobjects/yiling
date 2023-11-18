package com.yiling.admin.cms.document.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文献分页列表查询参数
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDocumentPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "标题")
    private String title;

    private Date startTime;

    private Date endTime;

    @ApiModelProperty(value = "状态 1未发布 2发布")
    private Integer status;
}