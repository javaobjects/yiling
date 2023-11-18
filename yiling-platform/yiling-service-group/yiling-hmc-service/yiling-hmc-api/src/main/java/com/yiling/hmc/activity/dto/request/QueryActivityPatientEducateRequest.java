package com.yiling.hmc.activity.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询患教活动request
 *
 * @author: fan.shen
 * @date: 2022/09/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryActivityPatientEducateRequest extends QueryPageListRequest {

    /**
     * 活动名称
     */
    private String activityName;

}
