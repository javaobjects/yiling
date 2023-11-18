package com.yiling.cms.collect.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCollectRequest extends BaseRequest {
    /**
     * 内容/会议/文献id
     */
    private Long collectId;

    /**
     * 收藏的类型：1-文章 2-视频 3-文献 4-会议
     */
    private Integer collectType;

    /**
     * 收藏状态：1-收藏 2-取消收藏
     */
    private Integer status;

    /**
     * 反馈来源
     */
    private Integer source;

    /**
     * 各业务线引用id
     */
    private Long cmsId;
}