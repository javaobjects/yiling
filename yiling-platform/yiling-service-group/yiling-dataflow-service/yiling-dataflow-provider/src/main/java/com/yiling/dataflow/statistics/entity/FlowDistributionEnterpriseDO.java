package com.yiling.dataflow.statistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业信息配置表
 * </p>
 *
 * @author handy
 * @date 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_distribution_enterprise")
public class FlowDistributionEnterpriseDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
