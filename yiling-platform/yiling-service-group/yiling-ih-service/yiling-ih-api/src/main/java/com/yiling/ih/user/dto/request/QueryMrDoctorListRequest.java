package com.yiling.ih.user.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询医药代表关联的医生列表 Request
 *
 * @author: xuan.zhou
 * @date: 2022/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMrDoctorListRequest extends BaseRequest {

    /**
     * 医药代表ID
     */
    @NotNull
    @Min(1L)
    private Long mrId;

    /**
     * 医生名称（模糊查询）
     */
    private String doctorName;
}
