package com.yiling.sales.assistant.banner.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaBannerReleaseRequest extends BaseRequest {
    private Long    id;
    /**
     * 操作:0 发布, 1 下架
     */
    private Integer releaseType;
    //================================
    /**
     * 发布状态：1-待发布 2-已发布 3-已停用
     */
    private Integer bannerStatus;

    private Long releaseUserId;

    private String releaseUserName;

    /**
     * 发布时间
     */
    private Date releaseTime;

    /**
     * 下架时间
     */
    private Date endTime;
}
