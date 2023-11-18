package com.yiling.ih.patient.dto;


import lombok.Data;


/**
 * 我的-我的医生-咨询过医生DTO
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcMyDoctorConsultationDetailDTO implements java.io.Serializable {

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 头像
     */
    private String picture;

    /**
     * 医生姓名
     */
    private String name;

    /**
     * 职称
     */
    private String professionName;

    /**
     * 科室名称
     */
    private String departmentName;

    /**
     * 医院名称
     */
    private String hospitalName;
}
