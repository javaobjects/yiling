package com.yiling.dataflow.crm.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseSimpleRegionInfoDTO extends BaseDTO {

    /**
     * crm系统对应客户编码
     */
    private String code;

    /**
     * crm系统对应客户名称
     */
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域名称
     */
    private String regionName;

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

}
