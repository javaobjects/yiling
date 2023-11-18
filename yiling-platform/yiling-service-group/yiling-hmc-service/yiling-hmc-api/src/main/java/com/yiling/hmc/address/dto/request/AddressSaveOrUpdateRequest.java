package com.yiling.hmc.address.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddressSaveOrUpdateRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 收货人名称
     */
    private String name;

    /**
     * 收货人手机号
     */
    private String mobile;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区县编码
     */
    private String regionCode;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区县名称
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
