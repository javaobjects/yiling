package com.yiling.admin.hmc.welfare.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/27
 */
@Data
public class EnterpriseListVO extends BaseVO {

    @ApiModelProperty("商家id")
    private Long eid;

    @ApiModelProperty("商家名称")
    private String ename;
}
