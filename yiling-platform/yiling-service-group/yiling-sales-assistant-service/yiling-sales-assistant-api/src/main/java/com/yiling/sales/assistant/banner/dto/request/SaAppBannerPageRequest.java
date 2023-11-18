package com.yiling.sales.assistant.banner.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaAppBannerPageRequest extends QueryPageListRequest {

    /**
     * 使用位置：0-所有 1-B2B移动端主Banner 2-B2B移动端副Banner
     */
    private Integer usageScenario;

    /**
     * 使用场景：1-以岭内部机构 2-非以岭机构
     */
    private Integer bannerCondition;

    /**
     * banner标题
     */
    private String  title;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer bannerStatus;

    private Date    createStartTime;

    private Date    createEndTime;

    private Date    useStartTime;

    private Date    useEndTime;
}
