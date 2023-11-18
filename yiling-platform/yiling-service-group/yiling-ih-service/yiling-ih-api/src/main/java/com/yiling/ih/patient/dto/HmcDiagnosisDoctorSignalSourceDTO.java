package com.yiling.ih.patient.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * HMC获取医生号源列表DTO
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcDiagnosisDoctorSignalSourceDTO implements java.io.Serializable {

    /**
     * 日期
     */
    private String dateTime;

    /**
     * 精确日期
     */
    private Date date;

    /**
     * 所属时段集合
     */
    private List<HmcDoctorSignalSourceBelongTimeDTO> belongTimeList;
}
