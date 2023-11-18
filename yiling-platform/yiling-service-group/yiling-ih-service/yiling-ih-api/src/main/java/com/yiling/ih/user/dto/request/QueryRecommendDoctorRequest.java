package com.yiling.ih.user.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: fan.shen
 * @data: 2022-12-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryRecommendDoctorRequest extends BaseRequest {

    /**
     * 科室
     */
    private List<Integer> departmentIds;

}
