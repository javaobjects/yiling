package com.yiling.user.enterprise.dto.request;

import lombok.Data;

/**
 * 创建标签 Request
 *
 * @author: lun.yu
 * @date: 2021/10/27
 */
@Data
public class UpdateTagsRequest extends CreateTagsRequest {

    /**
     * ID
     */
    private Long id;


}
