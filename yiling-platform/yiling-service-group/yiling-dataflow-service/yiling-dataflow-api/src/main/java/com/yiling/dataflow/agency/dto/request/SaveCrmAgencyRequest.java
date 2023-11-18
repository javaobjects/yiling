package com.yiling.dataflow.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmAgencyRequest extends BaseRequest {
    /**
     * 机构名称
     */
    private String name;
    //crm编码
    private String code;
    private String licenseNumber;
    /**
     * 药品经营许可证编号
     */
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    private String institutionPracticeLicense;

    /**
     * 以岭编码
     */
    private String ylCode;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 备注
     */
    private String businessRemark;

    /**
     * 曾用名
     */
    private String formerName;

    /**
     *所属省份名称
     */
    private String provinceName;
    /**
     *所属城市名称
     */
    private String cityName;
    /**
     *所属区区域名称
     */
    private String regionName;
    /**
     *详细地址
     */
    private String address;
    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 电话
     */
    private String phone;

    /**
     * 传真
     */
    private String fax;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;

    /**
     * 机构简称
     */
    private String shortName;
}
