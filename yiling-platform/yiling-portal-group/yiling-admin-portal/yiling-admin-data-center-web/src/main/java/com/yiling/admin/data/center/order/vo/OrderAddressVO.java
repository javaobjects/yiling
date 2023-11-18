package com.yiling.admin.data.center.order.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 收货地址
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderAddressVO extends BaseVO {
    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String name;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String mobile;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;
}
