package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Data
@Accessors(chain = true)
public class GetAppContentRequest extends BaseRequest {
    private static final long serialVersionUID = 6680164794948618214L;
    private Long id;

    private Integer businessLine;
}