package com.yiling.dataflow.crm.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/12/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseSimpleDTO extends BaseDTO {
    /**
     * crm系统对应客户编码
     */
    private String code;

    /**
     * crm系统对应客户名称
     */
    private String name;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市名称
     */
    private String cityName;

    /**
     *区名称
     */
    private String regionName;

    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;
}
