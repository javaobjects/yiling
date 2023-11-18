package com.yiling.ih.user.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询医药代表关联的医生列表 Request
 *
 * @author: benben.jia
 * @date: 2022/6/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAppMrDoctorListRequest extends QueryPageListRequest {

    /**
     * 医药代表ID
     */
    @NotNull
    @Min(1L)
    private Long mrId;

    /**
     * 认证状态 1已认证 2认证中（待审核）3认证未通过 4未认证
     */
    private Integer status;
}
