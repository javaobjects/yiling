package com.yiling.cms.collect.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/8/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCollectRequest extends BaseRequest {
    private static final long serialVersionUID = -3931272550971719024L;
    /**
     * 内容/会议/文献id
     */
    private Long collectId;

    /**
     * 收藏的类型：1-文章 2-视频 3-文献 4-会议
     */
    private Integer collectType;


    /**
     * 反馈来源
     */
    private Integer source;
}