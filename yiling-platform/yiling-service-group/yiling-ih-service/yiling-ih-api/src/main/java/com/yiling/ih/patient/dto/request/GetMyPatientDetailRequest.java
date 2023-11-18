package com.yiling.ih.patient.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取患者信息 Request
 *
 * @author: fan.shen
 * @date: 2022-09-13
 */
@Data
@Accessors(chain = true)
public class GetMyPatientDetailRequest implements java.io.Serializable {

    private Integer fromUserId;

    private Integer patientId;

}
