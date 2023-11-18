package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FlowAnalyseEnterpriseVo {
    @ApiModelProperty(value = "经销商ID")
    private Long eid;
    @ApiModelProperty(value = "经销商名称")
    private String ename;
    @ApiModelProperty(value = "机构编码")
    private Long crmEnterpriseId;
}
