package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 查询医药代表关联的医生列表-医生信息 DTO
 *
 * @author: benben.jia
 * @date: 2022/6/14
 */
@Data
public class DoctorListAppInfoDTO implements java.io.Serializable {

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
     * 驳回原因
     */
    private String failReviewReason;

}
