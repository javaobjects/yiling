package com.yiling.hmc.wechat.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/25
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceFetchPlanPageRequest extends QueryPageListRequest {

    /**
     * 初始拿药时间-起始
     */
    private Date initFetchStartTime;

    /**
     * 初始拿药时间-截止
     */
    private Date initFetchStopTime;

    /**
     * 拿药状态 1-已拿，2-未拿，3-已申请待拿
     */
    private Integer fetchStatus;
}
