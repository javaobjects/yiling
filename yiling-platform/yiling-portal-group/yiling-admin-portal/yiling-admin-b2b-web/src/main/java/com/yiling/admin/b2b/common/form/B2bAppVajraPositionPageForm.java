package com.yiling.admin.b2b.common.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppVajraPositionPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "金刚位标题")
    private String title;

    @ApiModelProperty(value = "状态：0-全部 1-启用 2-停用")
    private Integer vajraStatus;
}
