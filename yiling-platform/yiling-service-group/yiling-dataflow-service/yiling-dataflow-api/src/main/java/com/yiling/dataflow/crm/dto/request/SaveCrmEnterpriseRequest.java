package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * @author: shuang.zhang
 * @date: 2023/1/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmEnterpriseRequest extends BaseRequest {

    /**
     * 名称
     */
    private String name;
    /**
     * 统一信用代码
     */
    private String licenseNumber;
    /**
     * 省
     */
    private String provinceName;
    /**
     * 市
     */
    private String cityName;
    /**
     * 区
     */
    private String regionName;
    /**
     * 地址
     */
    private String address;
    /**
     * 类型 企业类型CrmSupplyChainRoleEnum
     */
    private Integer supplyChainRole;
    /**
     * 电话
     */
    private String phone;
    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;

    /**
     * 省
     */

    private String provinceCode;
    /**
     * 市
     */
    private String cityCode;
    /**
     * 区
     */
    private String regionCode;

    /**
     * 数据来源1-crm 2-团购 3-行业库
     */
    private Integer sourceFlag;

    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    private String institutionPracticeLicense;

    /**
     * 经营范围
     */
    private String businessScope;
    /**
     * CRM编码
     */
    private String code;

    private String fax;
    /**
     *  以岭编码
     */
    private String ylCode;
    /**
     * 备注
     */
    private String businessRemark;
    /**
     * 曾用名
     */
    private String formerName;
    /**
     * 邮编
     */
    private String postalCode;
    /**
     * 机构简称
     */
    private String shortName;
}
