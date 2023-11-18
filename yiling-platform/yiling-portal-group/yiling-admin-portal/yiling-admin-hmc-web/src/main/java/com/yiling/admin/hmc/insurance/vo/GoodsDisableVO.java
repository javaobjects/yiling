package com.yiling.admin.hmc.insurance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/1
 */
@Data
public class GoodsDisableVO {

    @ApiModelProperty(value = "是否允许被选择 0-可 1-不可")
    private Integer isAllowSelect;
}
