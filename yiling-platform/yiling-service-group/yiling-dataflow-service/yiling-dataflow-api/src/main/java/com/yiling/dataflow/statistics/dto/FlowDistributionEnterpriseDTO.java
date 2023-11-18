package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/2/7
 */
@Data
public class FlowDistributionEnterpriseDTO extends BaseDTO {

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 企业编码
     */
    private Long eid;

    /**
     * 企业crm编码
     */
    private String code;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 企业体系
     */
    private String enameGroup;

    /**
     * 企业等级
     */
    private String enameLevel;

}
