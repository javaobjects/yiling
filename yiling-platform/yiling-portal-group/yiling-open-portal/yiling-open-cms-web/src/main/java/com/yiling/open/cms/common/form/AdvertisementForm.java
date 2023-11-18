package com.yiling.open.cms.common.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @date: 2022/3/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdvertisementForm extends BaseForm {

    @ApiModelProperty(value = "投放位置:3-医生端首页 4-医生端络病学院")
    private Integer position;

}
