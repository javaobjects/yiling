package com.yiling.hmc.common.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdvertisementForm extends BaseForm {

    @ApiModelProperty(value = "投放位置:1-C端用户侧首页 2-C端用户侧我的")
    private Integer position;

}
