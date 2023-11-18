package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 创建标签 Request
 *
 * @author: lun.yu
 * @date: 2021/10/27
 */
@Data
public class CreateTagsRequest extends BaseRequest {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 类型：1-手动标签 2-自动标签
     */
    private Integer type;

}
