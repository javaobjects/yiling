package com.yiling.data.center.admin.enterprisecustomer.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业客户信息 VO
 *
 * @author: lun.yu
 * @date: 2021/11/1
 */
@Data
public class CustomerVO {

    @ApiModelProperty("企业ID")
    private Long id;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("企业类型")
    private Integer type;

    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    @ApiModelProperty("企业地址")
    private String address;

    @ApiModelProperty("联系人")
    private String contactor;

    @ApiModelProperty("联系人电话")
    private String contactorPhone;

    @ApiModelProperty("客户分组ID")
    private Long customerGroupId;

}
