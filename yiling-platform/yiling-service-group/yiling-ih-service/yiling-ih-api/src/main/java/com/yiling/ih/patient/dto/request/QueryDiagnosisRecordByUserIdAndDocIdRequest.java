package com.yiling.ih.patient.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询医患之间的问诊单
 *
 * @author: fan.shen
 * @date: 2023/4/7
 */
@Data
@Accessors(chain = true)
public class QueryDiagnosisRecordByUserIdAndDocIdRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * hmc用户id
     */
    private Integer fromUserId;

    /**
     * 患者id
     */
    private Integer patientId;

    /**
     * 医生id
     */
    private Integer docId;

}