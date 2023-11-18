package com.yiling.ih.patient.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 处方详情 Request
 *
 * @author: fan.shen
 * @date: 2022-09-13
 */
@Data
@Accessors(chain = true)
public class GetPrescriptionDetailRequest implements java.io.Serializable {

    private Integer id;


}
