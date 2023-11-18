package com.yiling.admin.hmc.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医带患活动患者列表
 * @author: fan.shen
 * @data: 2023-02-02
 */
@Data
@Accessors(chain = true)
public class HmcActivityDocPatientVO extends BaseVO {

    @ApiModelProperty(value = "活动id")
    private Integer activityId;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("疾病标签")
    private String diseaseTags;

    @ApiModelProperty("药品标签")
    private String medicationTags;

    @ApiModelProperty("医生id")
    private Integer doctorId;

    @ApiModelProperty("医院名称")
    private String hospitalName;

    @ApiModelProperty("医院省份")
    private String province;

    @ApiModelProperty("医生名称")
    private String doctorName;

    @ApiModelProperty("是否是新用户 1-是 2-不是")
    private Integer userState;

    @ApiModelProperty("绑定时间")
    private Date createTime;

    @ApiModelProperty("提审时间")
    private Date arraignmentTime;

    @ApiModelProperty("凭证状态 1-待上传 2-待审核 3-审核通过 4-审核驳回")
    private Integer certificateState;

    @ApiModelProperty("审核时间")
    private Date auditTime;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

    @ApiModelProperty(value = "患者id",hidden = true)
    private Integer patientId;



}
