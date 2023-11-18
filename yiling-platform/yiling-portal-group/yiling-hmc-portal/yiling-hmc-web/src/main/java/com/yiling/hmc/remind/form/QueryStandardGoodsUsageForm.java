package com.yiling.hmc.remind.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询标准库用法用量
 * @author: fan.shen
 * @date: 2022/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStandardGoodsUsageForm extends BaseForm {

    /**
     * 标准库id
     */
    @ApiModelProperty(value = "标准库id")
   private Long id;

}
