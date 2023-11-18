package com.yiling.b2b.app.deliveryAddress.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配送地址表
 * </p>
 *
 * @author gxl
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeliveryAddressVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String receiver;

    /**
     * 收货人手机号
     */
    @ApiModelProperty(value = "收货人手机号")
    private String mobile;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 是否默认：0-否 1-是
     */
    @ApiModelProperty(value = "是否默认：0-否 1-是")
    private Integer defaultFlag;
}
