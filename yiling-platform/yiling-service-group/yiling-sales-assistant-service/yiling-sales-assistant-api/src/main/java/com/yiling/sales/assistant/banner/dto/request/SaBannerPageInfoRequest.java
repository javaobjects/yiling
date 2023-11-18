package com.yiling.sales.assistant.banner.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaBannerPageInfoRequest extends QueryPageListRequest {
    private String title;

    private Date releaseStartTime;

    private Date releaseStopTime;

    private Date endStartTime;

    private Date endStopTime;

    private Integer bannerCondition;

    private Long eid;

    private Integer bannerStatus;
}
