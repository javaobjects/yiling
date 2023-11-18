package com.yiling.dataflow.crm.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/9 0009
 */
@Data
public class CrmEnterprisePartBO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    private Integer supplyChainRole;

    /**
     * crm系统对应客户名称
     */
    private String name;
    /**
     * crm编码
     */
    private String code;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

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
     * 地址
     */
    private String address;

    /**
     * 所属省份名称
     */
    private String provinceName;
    /**
     * 所属城市名称
     */
    private String cityName;
    /**
     * 所属区区域名称
     */
    private String regionName;

    /**
     * 是否目标 1-是；2-否(零售机构专用)
     */
    private Integer targetFlag;

}
