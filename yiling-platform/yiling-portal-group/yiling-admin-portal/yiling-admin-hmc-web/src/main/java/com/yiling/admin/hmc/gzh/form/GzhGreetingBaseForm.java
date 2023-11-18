package com.yiling.admin.hmc.gzh.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 公众号欢迎语 Form
 *
 * @author: fan.shen
 * @date: 2023/3/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GzhGreetingBaseForm extends BaseForm {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

}
