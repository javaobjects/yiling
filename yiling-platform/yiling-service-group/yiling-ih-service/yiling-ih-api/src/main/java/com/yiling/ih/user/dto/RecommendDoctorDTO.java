package com.yiling.ih.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 医生信息 DTO
 *
 * @author: fan.shen
 * @date: 2022-10-24
 */
@NoArgsConstructor
@Data
public class RecommendDoctorDTO implements Serializable {

    private static final long serialVersionUID = -2295160299359559926L;


    /**
     * 科室id
     */
    private Integer departmentId;

    /**
     * 	父级科室id
     */
    private Integer departmentParentId;

    /**
     * 	问诊量
     */
    private Integer diagnosticCount;

    /**
     * 	医生名称
     */
    private String doctorName;

    /**
     * 	所在医疗机构
     */
    private String hospitalName;

    /**
     * id
     */
    private Integer id;

    /**
     * 	医生头像
     */
    private String picture;

    /**
     * 	职称
     */
    private String profession;

    /**
     * 	问诊费
     */
    private String registerFee;
}
