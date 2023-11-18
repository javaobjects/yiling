package com.yiling.user.integral.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 修改收货地址信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateReceiptAddressRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 收货人
     */
    private String contactor;

    /**
     * 联系电话
     */
    private String contactorPhone;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 详细地址
     */
    private String address;

}