package com.yiling.f2b.web.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
public class EnterpriseInfoVO implements java.io.Serializable {
    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;
}
