package com.yiling.user.enterprise.dto.request;

import java.util.Objects;

import lombok.Data;

/**
 * 修改企业资质管理信息 Request
 *
 * @author: lun.yu
 * @date: 2021/10/21
 */
@Data
public class UpdateEnterpriseInfoRequest {

    /**
     * 企业名称
     */
    private String name;

    /**
     * 联系人
     */
    private String contactor;

    /**
     * 联系人电话
     */
    private String contactorPhone;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateEnterpriseInfoRequest that = (UpdateEnterpriseInfoRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(licenseNumber, that.licenseNumber) &&
                Objects.equals(provinceCode, that.provinceCode) && Objects.equals(cityCode, that.cityCode) && Objects.equals(regionCode, that.regionCode);
    }

    public boolean equalsTo(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        UpdateEnterpriseInfoRequest that = (UpdateEnterpriseInfoRequest) o;
        return Objects.equals(contactor, that.contactor) && Objects.equals(contactorPhone, that.contactorPhone);
    }

}
