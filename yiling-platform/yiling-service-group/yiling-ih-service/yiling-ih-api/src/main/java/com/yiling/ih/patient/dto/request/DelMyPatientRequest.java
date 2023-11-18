package com.yiling.ih.patient.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 删除患者信息 Request
 *
 * @author: fan.shen
 * @date: 2022-09-13
 */
@Data
@Accessors(chain = true)
public class DelMyPatientRequest implements java.io.Serializable {

    private Integer fromUserId;

    private Integer patientId;

}
