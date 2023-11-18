package com.yiling.mall.banner.dto.request;

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
public class B2bAppVajraPositionPageRequest extends QueryPageListRequest {

    /**
     * 来源1-POP 2-销售助手 3-B2B
     */
    private Integer source;

    /**
     * 金刚位标题
     */
    private String title;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer vajraStatus;
}
