package com.yiling.admin.hmc.welfare.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/27
 */
@Data
public class DrugWelfareListVO extends BaseVO {

    @ApiModelProperty("福利计划名称")
    private String name;
}
