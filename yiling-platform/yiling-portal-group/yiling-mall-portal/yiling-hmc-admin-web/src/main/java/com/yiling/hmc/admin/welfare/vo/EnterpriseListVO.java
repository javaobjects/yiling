package com.yiling.hmc.admin.welfare.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: benben.jia
 * @data: 2022/09/28
 */
@Data
public class EnterpriseListVO extends BaseVO {

    @ApiModelProperty("商家id")
    private Long eid;

    @ApiModelProperty("商家名称")
    private String ename;
}
