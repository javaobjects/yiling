package com.yiling.b2b.admin.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
public class EnterpriseInfoVO implements java.io.Serializable {

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "联系人")
    private String contactor;

    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

    @ApiModelProperty(value = "详细地址")
    private String address;
}
