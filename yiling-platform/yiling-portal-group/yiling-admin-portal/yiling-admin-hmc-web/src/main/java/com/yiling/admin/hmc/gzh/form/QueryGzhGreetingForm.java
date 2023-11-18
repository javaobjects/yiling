package com.yiling.admin.hmc.gzh.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询公众号欢迎语 Form
 *
 * @author: fan.shen
 * @date: 2023/3/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGzhGreetingForm extends QueryPageListForm {

    /**
     * 场景id
     */
    @ApiModelProperty("场景id")
    private Integer sceneId;

}
