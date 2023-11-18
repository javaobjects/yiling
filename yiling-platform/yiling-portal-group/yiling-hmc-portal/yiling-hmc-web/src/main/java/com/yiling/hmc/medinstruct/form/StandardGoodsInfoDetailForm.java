package com.yiling.hmc.medinstruct.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsInfoDetailForm extends BaseForm {

    private static final long serialVersionUID = -333710304281212221L;

    /**
     * 标准库ID
     */
    @ApiModelProperty(value = "标准库ID")
    private Long id;

}
