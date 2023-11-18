package com.yiling.mall.banner.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppHotWordsPageRequest extends QueryPageListRequest {

    /**
     * 来源1-POP 2-销售助手 3-B2B
     */
    private Integer hotWordsSource;

    /**
     * 热词名称
     */
    private String content;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer useStatus;

    private Date createStartTime;

    private Date createEndTime;

    private Date useStartTime;

    private Date useEndTime;
}
