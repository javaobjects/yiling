package com.yiling.ih.user.feign.dto.response;

import java.util.List;

import lombok.Data;

/**
 * 查询医药代表关联的医生列表-医生信息 DTO
 *
 * @author: benben.jia
 * @date: 2022/6/14
 */
@Data
public class GetDoctorInfoByDoctorIdResponse implements java.io.Serializable {

    private static final long serialVersionUID = 7707287975078132869L;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     *医生名称
     */
    private String doctorName;

    /**
     * 头像
     */
    private String picture;

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
    private String cellularphone;

    /**
     *身份证号
     */
    private String number;

    /**
     *身份证正面
     */
    private String identityfront;

    /**
     *身份证反面
     */
    private String identityreverse;

    /**
     *执业证书照片集合
     */
    private List<String> doctorsnoteList;

    /**
     *执业证书编号
     */
    private String doctorsnoteCertNo;

    /**
     *医师资格证书集合
     */
    private List<String> certificateList;

    /**
     *医师资格证书编号
     */
    private String certificateCertNo;

}
