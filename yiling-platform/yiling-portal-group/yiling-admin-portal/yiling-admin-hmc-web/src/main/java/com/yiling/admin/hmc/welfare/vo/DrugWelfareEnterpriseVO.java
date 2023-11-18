package com.yiling.admin.hmc.welfare.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/27
 */
@Data
public class DrugWelfareEnterpriseVO extends BaseVO {

    @ApiModelProperty("商家名称")
    private String ename;

    @ApiModelProperty("福利计划id")
    private Long drugWelfareId;

    @ApiModelProperty("福利计划名称")
    private String drugWelfareName;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
