package com.yiling.sjms.agency.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shuan
 */
@Data
public class EnterpriseDisableVO {

    @ApiModelProperty(value = "非锁销量分配商业公司是否已经占用 true不可用 false可用")
    private Boolean unlockBusinessDisable;

    @ApiModelProperty(value = "占用描述")
    private String unlockBusinessDesc;

    @ApiModelProperty(value = "非锁销量分配客户公司是否已经占用 true不可用 false可用")
    private Boolean unlockCustomerDisable;

    @ApiModelProperty(value = "占用描述")
    private String unlockCustomerDesc;
}
