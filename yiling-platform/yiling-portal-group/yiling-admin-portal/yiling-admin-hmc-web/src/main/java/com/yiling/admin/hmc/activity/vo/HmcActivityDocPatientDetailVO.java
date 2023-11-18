package com.yiling.admin.hmc.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 医带患活动患者详情
 * @author: fan.shen
 * @data: 2023-02-02
 */
@Data
@Accessors(chain = true)
public class HmcActivityDocPatientDetailVO extends BaseVO {

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

    @ApiModelProperty("医生名称")
    private String doctorName;

    @ApiModelProperty("提审时间")
    private Date arraignmentTime;

    @ApiModelProperty("凭证状态 1-待上传 2-待审核 3-审核通过 4-审核驳回")
    private Integer certificateState;

    @ApiModelProperty("处方图片地址")
    private List<String> picture;

    @ApiModelProperty("审核日志集合")
    private List<HmcActivityDocPatientCheckLogVO> logList;

}
