package com.yiling.ih.user.feign.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.ih.common.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据医药代表id获取医生列表 Request
 *
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetDoctorListByAgentIdRequest extends BaseRequest {

    /**
     * 药代ID
     */
    @NotNull
    private Long agentId;

    /**
     * 	医生名称
     */
    private String doctorName;

}
