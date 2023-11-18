package com.yiling.admin.cms.question.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2022/06/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsInfoForm extends QueryPageListForm {

    private static final long serialVersionUID = -33371030428332221L;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

}
