package com.yiling.sjms.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteHosDruRelFormAppendixRequest extends BaseRequest {

    private static final long serialVersionUID = 4873478009483624505L;

    private Long id;
}
