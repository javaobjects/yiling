package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/23 0023
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveAgencyFormRequest extends BaseRequest {

    /**
     * 机构新增修改表 id
     */
    private Long id;
}
