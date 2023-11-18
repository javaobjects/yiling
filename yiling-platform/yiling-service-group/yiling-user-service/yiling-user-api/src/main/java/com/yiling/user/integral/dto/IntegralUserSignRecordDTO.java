package com.yiling.user.integral.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户签到记录表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralUserSignRecordDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    private Integer platform;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 签到日期
     */
    private Date signTime;

    /**
     * 当前连续签到天数
     */
    private Integer continueDays;

    /**
     * 签到积分
     */
    private Integer signIntegral;

    /**
     * 连续签到奖励积分
     */
    private Integer continueSignIntegral;

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
