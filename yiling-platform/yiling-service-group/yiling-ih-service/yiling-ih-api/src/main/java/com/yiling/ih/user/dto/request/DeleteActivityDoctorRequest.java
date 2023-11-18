package com.yiling.ih.user.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class DeleteActivityDoctorRequest extends BaseRequest {

    private static final long serialVersionUID = 5197087711756275254L;

    private Integer activityId;

    private Integer doctorId;

    /**
     * 	医生资格 1恢复 2取消
     */
    private Integer doctorStatus;
}
