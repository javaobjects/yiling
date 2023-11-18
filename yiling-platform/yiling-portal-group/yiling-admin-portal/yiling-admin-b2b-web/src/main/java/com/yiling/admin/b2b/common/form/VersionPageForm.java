package com.yiling.admin.b2b.common.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页查询版本列表
 *
 * @author: yong.zhang
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class VersionPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "App类型：0-全部 1-android 2-ios")
    private Integer appType;
}
