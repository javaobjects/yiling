package com.yiling.hmc.patient.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询患者分页列表
 * @author: gxl
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPatientPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -9043578662422413637L;

    /**
     * 患者年龄
     */
    private Integer startPatientAge;

    private Integer endPatientAge;

    /**
     * 患者性别
     */
    private Integer gender;

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 省code
     */
    private String provinceCode;

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 区code
     */
    private String regionCode;

    private Date startTime;

    private Date endTime;
}