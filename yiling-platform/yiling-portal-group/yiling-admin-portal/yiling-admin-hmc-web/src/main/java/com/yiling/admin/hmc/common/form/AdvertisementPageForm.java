package com.yiling.admin.hmc.common.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdvertisementPageForm extends QueryPageListForm {

    @ApiModelProperty("投放位置:1-C端用户侧首页 2-C端用户侧我的")
    private Integer position;
}
