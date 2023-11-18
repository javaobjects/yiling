package com.yiling.dataflow.flow.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author shichen
 * @类名 FlowCrmEnterpriseDTO
 * @描述
 * @创建时间 2022/7/13
 * @修改人 shichen
 * @修改时间 2022/7/13
 **/
@Data
public class FlowCrmEnterpriseDTO extends BaseDTO {
    /**
     * 企业crm名称
     */
    private String crmName;

    /**
     * 企业crmCode
     */
    private String crmCode;

    /**
     * dems系统编码
     */
    private String crmNumber;
}
