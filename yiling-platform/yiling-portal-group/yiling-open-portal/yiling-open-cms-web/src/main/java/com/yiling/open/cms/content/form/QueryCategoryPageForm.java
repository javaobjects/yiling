package com.yiling.open.cms.content.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCategoryPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "引用业务线id 1-健康管理中心，2-IH医生端，3-IH患者端", required = true)
    @NotNull
    private Long lineId;

    /**
     * 板块id
     */
    @ApiModelProperty(value = "板块id")
    private Long moduleId;
}
