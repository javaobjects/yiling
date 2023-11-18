package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class UpdateDeliveryAddressRequest extends BaseRequest {

    private static final long serialVersionUID = 5086910206294791692L;

    /**
     * 收货地址ID
     */
    private Long id;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收货人手机号
     */
    private String mobile;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 是否默认：0-否 1-是
     */
    private Integer defaultFlag;

}
