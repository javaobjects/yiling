package com.yiling.sjms.crm.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryCrmGoodsCategoryForm
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmGoodsCategoryForm extends BaseForm {

    /**
     * 品类编码
     */
    @ApiModelProperty(value = "品类编码")
    private String code;

    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String name;

    /**
     * 分层级别
     */
    @ApiModelProperty(value = "分层级别")
    private Integer categoryLevel;

    /**
     * 上级分类名称
     */
    @ApiModelProperty(value = "上级分类名称")
    private String parentName;
}
