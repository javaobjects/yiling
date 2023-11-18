package com.yiling.admin.hmc.patient.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 患者信息
 *
 * @author: gxl
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PatientVO extends BaseVO {
    /**
     * 患者名称
     */
    @ApiModelProperty(value = "患者名称")
    private String patientName;

    /**
     * 患者关联的用户id
     */
    @ApiModelProperty(value = "患者关联的用户id")
    private Long fromUserId;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    /**
     * 患者年龄
     */
    @ApiModelProperty(value = "患者年龄")
    private Integer patientAge;

    /**
     * 患者性别
     */
    @ApiModelProperty(value = "患者性别 0-未知 1-男 2-女")
    private Integer gender;

    /**
     * 省code
     */
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 市code
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 区code
     */
    @ApiModelProperty(value = "区")
    private String region;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 省code
     */
    @ApiModelProperty(hidden = true)
    private String provinceCode;

    /**
     * 市code
     */
    @ApiModelProperty(hidden = true)
    private String cityCode;

    /**
     * 区code
     */
    @ApiModelProperty(hidden = true)
    private String regionCode;

    private String mobile;

    /**
     * 患者绑定的用户数量
     */
    @ApiModelProperty(value = "患者绑定的用户数量")
    private Long bindUserCount;

    /**
     * 患者绑定的医生数量
     */
    @ApiModelProperty(value = "患者绑定的医生数量")
    private Long bindDoctorCount;

    @ApiModelProperty(value = "药品标签")
    private String medicationTags;

    @ApiModelProperty(value = "疾病标签")
    private String  diseaseTags;
}