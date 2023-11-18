package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
@ToString
public class AddDeliveryAddressRequest extends BaseRequest {

    private static final long serialVersionUID = 3545431035155324800L;

    /**
     * 企业id
     */
    private Long eid;

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
    @Deprecated
    private String provinceCode;

    /**
     * 省份名称
     */
    @Deprecated
    private String provinceName;

    /**
     * 城市编码
     */
    @Deprecated
    private String cityCode;

    /**
     * 城市名称
     */
    @Deprecated
    private String cityName;

    /**
     * 区域编码
     */
    @Deprecated
    private String regionCode;

    /**
     * 区域名称
     */
    @Deprecated
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
