package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据id删除
 * @author: gxl
 * @date: 2023/2/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteAgencyLockFormRequest extends BaseRequest {

    private static final long serialVersionUID = -7865460597856242347L;

    private Long id;
}