package com.yiling.ih.user.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医带患患者审核
 * @author: fan.shen
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class ActivityDocPatientAuditRequest implements java.io.Serializable{

    private static final long serialVersionUID = -544667472485532361L;

    /**
     * id
     */
    private Integer id;

    /**
     *审核结果 1-通过 2-驳回
     */
    private Integer checkResult;

    /**
     *驳回原因
     */
    private String rejectReason;

    /**
     *操作人id
     */
    private Integer createUser;
}
