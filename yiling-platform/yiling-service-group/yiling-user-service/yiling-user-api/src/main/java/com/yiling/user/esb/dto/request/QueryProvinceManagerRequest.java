package com.yiling.user.esb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据部门id和省区查询负责人
 * @author: gxl
 * @date: 2023/2/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryProvinceManagerRequest extends BaseRequest {
    private static final long serialVersionUID = 1026989021903941624L;

    private String provinceName;

    private Long orgId;
}