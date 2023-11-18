package com.yiling.hmc.activity.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 查询患教活动request
 *
 * @author: fan.shen
 * @date: 2022/09/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryActivityRequest extends QueryPageListRequest {

    /**
     * 活动idList
     */
    private List<Long> activityIdList;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动类型 1-医带患
     */
    private Integer activityType;

}
