package com.yiling.ih.user.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 医带患活动患者列表
 *
 * @author: fan.shen
 * @data: 2023/02/01
 */
@Data
@Accessors(chain = true)
public class PatientCheckLogDTO extends BaseDTO {

    private static final long serialVersionUID = 6232095217976779033L;

    /**
     * 提审时间
     */
    private Date arraignmentTime;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 创建用户
     */
    private Integer createUser;

    /**
     * 审核结果 1-通过 2-驳回
     */
    private Integer checkResult;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 处方图片集合
     */
    private List<String> picture;
}
