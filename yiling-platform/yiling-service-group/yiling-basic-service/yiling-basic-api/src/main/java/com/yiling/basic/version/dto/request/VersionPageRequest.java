package com.yiling.basic.version.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class VersionPageRequest extends QueryPageListRequest {

    /**
     * App类型：0-全部 1-android 2-ios
     */
    private Integer appType;

    /**
     * 渠道
     */
    private String  channelCode;
}
