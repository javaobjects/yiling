package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 提交审核请求类
 * @author: xinxuan.jia
 * @date: 2023/6/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateSelectAppealFlowRequest extends BaseRequest {
    /**
     * 列表id
     */
    private Long id;

}
