package com.yiling.hmc.remind.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: fan.shen
 * @date: 2022/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardSpecificationPageForm extends QueryPageListForm {

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
   private String name;

}
