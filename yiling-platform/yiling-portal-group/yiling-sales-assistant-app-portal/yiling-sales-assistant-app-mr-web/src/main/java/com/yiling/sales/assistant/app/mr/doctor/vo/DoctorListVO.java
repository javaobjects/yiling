package com.yiling.sales.assistant.app.mr.doctor.vo;

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
public class DoctorListVO extends BaseVO {

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "医生头像")
    private String picture;

    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    @ApiModelProperty(value = "职称")
    private String profession;

    @ApiModelProperty(value = "所在医疗机构")
    private String hospitalName;

    @ApiModelProperty(value = "驳回原因")
    private String failReviewReason;

}
