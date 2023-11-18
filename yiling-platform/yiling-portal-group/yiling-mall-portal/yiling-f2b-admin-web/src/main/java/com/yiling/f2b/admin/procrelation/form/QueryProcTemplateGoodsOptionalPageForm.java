package com.yiling.f2b.admin.procrelation.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryProcTemplateGoodsOptionalPageForm extends QueryPageListForm {

    /**
     * 模板id
     */
    @Min(1)
    @NotNull
    @ApiModelProperty(value = "模板id")
    private Long templateId;

    /**
     * 工业id
     */
    @Min(1)
    @NotNull
    @ApiModelProperty(value = "工业id")
    private Long factoryEid;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    private String name;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号", example = "Z109090")
    private String licenseNo;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    @ApiModelProperty(value = "专利类型 0-全部 1-非专利 2-专利")
    private Integer isPatent;

}
