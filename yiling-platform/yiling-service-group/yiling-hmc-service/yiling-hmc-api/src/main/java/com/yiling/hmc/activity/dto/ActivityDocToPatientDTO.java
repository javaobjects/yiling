package com.yiling.hmc.activity.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患教活动DTO
 *
 * @author: fan.shen
 * @date: 2022/09/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityDocToPatientDTO extends BaseDTO {

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 运营备注
     */
    private String activityRemark;

    /**
     * 活动状态 1-启用，2-停用
     */
    private Integer activityStatus;

    /**
     * 活动头图
     */
    private String activityHeadPic;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 限制医生类型：1-手动配置
     */
    private Integer restrictDocType;

    /**
     * 限制用户类型：1-平台新用户（没有注册过的），2-不限制
     */
    private Integer restrictUserType;

    /**
     * 审核用户类型：1-需要审核，2-无需审核
     */
    private Integer auditUserType;


}
