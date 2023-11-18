package com.yiling.admin.erp.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySqlForm extends BaseForm {

    /**
     * sql
     */
    @NotNull
    @ApiModelProperty(value = "sql")
    private String sqlContext;

    /**
     * 数据长度
     */
    @NotNull
    @ApiModelProperty(value = "数据长度")
    private String sqlCount;

    /**
     * 数据长度
     */
    @NotNull
    @ApiModelProperty(value = "suId")
    private Long suId;
}
