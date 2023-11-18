package com.yiling.ih.patient.dto.request;

import lombok.Data;

/**
 * 已读上报
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class ReadReportRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 	群组id
     */
    private String groupId;

}
