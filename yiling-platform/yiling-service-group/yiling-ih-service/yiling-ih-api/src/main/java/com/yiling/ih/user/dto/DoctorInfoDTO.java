package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 医生信息 DTO
 *
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@Data
public class DoctorInfoDTO implements java.io.Serializable {

    private static final long serialVersionUID = -2295160299359559926L;

    /**
     * ID
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 职称
     */
    private String jobTitle;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构科室名称
     */
    private String deptName;

    /**
     * 认证状态
     */
    private String status;
}
