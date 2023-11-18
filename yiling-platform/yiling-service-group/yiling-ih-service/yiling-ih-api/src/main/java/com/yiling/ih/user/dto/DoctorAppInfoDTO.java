package com.yiling.ih.user.dto;

import java.util.List;

import lombok.Data;

/**
 * 查询医药代表关联的医生列表-医生信息 DTO
 *
 * @author: benben.jia
 * @date: 2022/6/14
 */
@Data
public class DoctorAppInfoDTO implements java.io.Serializable {

    private static final long serialVersionUID = 7707287975078132869L;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 医生头像
     */
    private String picture;

    /**
     *医生名称
     */
    private String doctorName;

    /**
     *职称
     */
    private String profession;

    /**
     *所在医疗机构
     */
    private String hospitalName;

    /**
     *所在医疗机构科室
     */
    private String hospitalDepartment;

    /**
     *医生手机号
     */
    private String mobile;

    /**
     *身份证号
     */
    private String number;

    /**
     *身份证正面
     */
    private String identityFront;

    /**
     *身份证反面
     */
    private String identityReverse;

    /**
     *执业证书照片集合
     */
    private List<String> doctorNoteList;

    /**
     *执业证书编号
     */
    private String doctorsNoteCertNo;

    /**
     *医师资格证书集合
     */
    private List<String> certificateList;

    /**
     *医师资格证书编号
     */
    private String certificateCertNo;

}
