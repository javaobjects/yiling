package com.yiling.user.integral.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分消耗规则 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralUseRuleDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    private Integer platform;

    /**
     * 执行状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 规则生效时间
     */
    private Date startTime;

    /**
     * 规则失效时间
     */
    private Date endTime;

    /**
     * 规则说明
     */
    private String description;

    /**
     * 行为ID
     */
    private Long behaviorId;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
