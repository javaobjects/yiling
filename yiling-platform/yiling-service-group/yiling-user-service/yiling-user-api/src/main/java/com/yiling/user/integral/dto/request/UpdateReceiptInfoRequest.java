package com.yiling.user.integral.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单收货信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateReceiptInfoRequest extends BaseRequest {

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

    /**
     * 快递公司（见字典）
     */
    private String expressCompany;

    /**
     * 快递单号
     */
    private String expressOrderNo;

}