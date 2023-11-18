package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 查询通过活动注册的用户列表
 *
 * @author xuan.zhou
 * @date 2023-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryActivityDoctorUserCountRequest extends QueryPageListRequest {

    /**
     * C端活动来源 1-患教活动，2-医带患，3-八子补肾
     */
    private Integer activitySource;

    /**
     * 医生idList
     */
    private List<Integer> doctorIdList;

}

