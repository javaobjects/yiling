package com.yiling.admin.hmc.doctor.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryDoctorPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "医院id")
    private Integer hospitalId;

    @ApiModelProperty(value = "来源：数据来源 0用户自主完善 1销售助手APP 2运营人员导入或创建")
    private Integer source;

    private Date startTime;

    private Date endTime;

    @ApiModelProperty(value = "当前活动id")
    private Integer activityId;

    @ApiModelProperty(value = "医生id")
    private Long doctorId;
}
