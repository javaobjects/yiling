package com.yiling.hmc.address.dto;

import com.yiling.framework.common.base.BaseDTO;

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
public class AddressDTO extends BaseDTO {

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 市code
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
     * 地址
     */
    private String address;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人手机号
     */
    private String mobile;

    /**
     * 是否默认：0-否 1-是
     */
    private Integer defaultFlag;
}
