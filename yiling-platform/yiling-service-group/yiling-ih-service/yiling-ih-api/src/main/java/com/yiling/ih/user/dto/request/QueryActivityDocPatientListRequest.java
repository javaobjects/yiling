package com.yiling.ih.user.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 医带患患者信息
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryActivityDocPatientListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -544667472485532361L;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     *患者名称
     */
    private String patientName;

    /**
     *医生id
     */
    private Integer doctorId;

    /**
     *医生名称
     */
    private String doctorName;

    /**
     *凭证状态 全部-0或者null 1-待上传 2-待审核 3-审核通过 4-审核驳回
     */
    private Integer certificateState;
}
