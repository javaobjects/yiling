package com.yiling.ih.user.feign.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.ih.common.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据医药代表id获取医生列表 Response
 *
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetDoctorListByAgentIdResponse extends BaseResponse {

    /**
     * 	医生ID
     */
    @JsonProperty("id")
    private Long id;

    /**
     * 医生手机号
     */
    @JsonProperty("cellularphone")
    private String cellularphone;

    /**
     * 	医生名称
     */
    @JsonProperty("doctorName")
    private String doctorName;

    /**
     * 	所在医疗机构科室
     */
    @JsonProperty("hospitalDepartment")
    private String hospitalDepartment;

    /**
     * 	所在医疗机构
     */
    @JsonProperty("hospitalName")
    private String hospitalName;

    /**
     * 	医生证件号（身份证号）
     */
    @JsonProperty("number")
    private String number;

    /**
     * 	职称
     */
    @JsonProperty("profession")
    private String profession;

    /**
     * 	资质认证
     */
    @JsonProperty("state")
    private String state;
}
