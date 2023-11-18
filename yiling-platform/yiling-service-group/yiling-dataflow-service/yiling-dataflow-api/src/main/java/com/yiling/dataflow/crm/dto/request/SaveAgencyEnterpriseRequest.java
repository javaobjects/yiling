package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/1 0001
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAgencyEnterpriseRequest extends BaseRequest {


    /**
     * 名称
     */
    private String name;
    /**
     * 统一信用代码
     */
    private String licenseNumber;

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
}
