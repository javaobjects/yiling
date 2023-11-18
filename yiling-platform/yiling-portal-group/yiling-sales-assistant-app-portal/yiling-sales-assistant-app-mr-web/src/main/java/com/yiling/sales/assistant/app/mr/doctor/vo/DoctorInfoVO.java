package com.yiling.sales.assistant.app.mr.doctor.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手APP-医生管理-医生信息 DTO
 * @author: benben.jia
 * @data: 2022/06/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DoctorInfoVO extends BaseVO {

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    @ApiModelProperty(value = "职称")
    private String profession;

    @ApiModelProperty(value = "所在医疗机构")
    private String hospitalName;

    @ApiModelProperty(value = "所在医疗机构科室")
    private String hospitalDepartment;

    @ApiModelProperty(value = "医生手机号")
    private String mobile;

    @ApiModelProperty(value = "医生证件号（身份证号）")
    private String number;

    @ApiModelProperty(value = "身份证正面")
    private String identityFront;

    @ApiModelProperty(value = "身份证反面")
    private String identityReverse;

    @ApiModelProperty(value = "执业证书照片集合")
    private List<String> doctorNoteList;

    @ApiModelProperty(value = "执业证书编号")
    private String doctorsNoteCertNo;

    @ApiModelProperty(value = "医师资格证书集合")
    private List<String> certificateList;

    @ApiModelProperty(value = "医师资格证书编号")
    private String certificateCertNo;


}
