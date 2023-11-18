package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 医生信息 DTO
 *
 * @author: fan.shen
 * @date: 2022-10-24
 */
@Data
public class HmcDoctorInfoDTO implements java.io.Serializable {

    private static final long serialVersionUID = -2295160299359559926L;

    /**
     * 医生id
     */
    private Integer id;


    /**
     * 医生名称
     */
    private String doctorName;

    /**
     * 职称
     */
    private String profession;

    /**
     * 所在医疗机构
     */
    private String hospitalName;

    /**
     * 所在医疗机构科室
     */
    private String hospitalDepartment;

    /**
     * 医生头像
     */
    private String picture;

    /**
     * 科室id
     */
    private Integer departmentId;

}
