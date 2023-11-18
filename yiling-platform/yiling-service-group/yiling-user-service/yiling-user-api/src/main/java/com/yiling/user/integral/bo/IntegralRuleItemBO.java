package com.yiling.user.integral.bo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分发放规则 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralRuleItemBO extends BaseDTO {

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
     * 执行进度：1-未开始 2-进行中 3-已结束
     */
    private Integer progress;

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
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String mobile;


}
