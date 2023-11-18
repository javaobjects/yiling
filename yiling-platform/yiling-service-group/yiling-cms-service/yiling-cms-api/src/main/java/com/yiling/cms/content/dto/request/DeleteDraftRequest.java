package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除草稿
 * @author: gxl
 * @date: 2022/3/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteDraftRequest extends BaseRequest {

    private static final long serialVersionUID = 2561034198133937307L;
    private Long id;
}