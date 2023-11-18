package com.yiling.sales.assistant.app.content.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 内容分页列表查询参数
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryContentPageForm extends QueryPageListForm {

    /**
     * 引用业务线id
     */
    @ApiModelProperty(value = "引用业务线id 1-健康管理中心，2-IH医生端，3-IH患者端 6-销售助手 7-大运河",required = true)
    @NotNull
    private Long lineId;

    /**
     * 板块id
     */
    @ApiModelProperty(value = "板块id")
    private Long moduleId;

    /**
     * 栏目id
     */
    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    @ApiModelProperty(value = "类型:1-文章 2-视频")
    private Integer contentType;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer isOpen;

    @ApiModelProperty(value = "视频标题或者医生名称")
    private String title;
}