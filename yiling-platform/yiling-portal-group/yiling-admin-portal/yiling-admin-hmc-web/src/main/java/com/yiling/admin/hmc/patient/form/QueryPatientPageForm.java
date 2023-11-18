package com.yiling.admin.hmc.patient.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询患者分页列表
 *
 * @author: gxl
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPatientPageForm extends QueryPageListForm {


    /**
     * 患者年龄
     */
    @ApiModelProperty(value = "年龄开始")
    private Integer startPatientAge;

    @ApiModelProperty(value = "年龄结束")
    private Integer endPatientAge;

    /**
     * 患者性别
     */
    @ApiModelProperty(value = "性别 1男 0女 全部传null")
    private Integer gender;

    /**
     * 患者名称
     */
    @ApiModelProperty(value = "姓名")
    private String patientName;

    /**
     * 省code
     */
    @ApiModelProperty(value = "省code")
    private String provinceCode;

    /**
     * 市code
     */
    @ApiModelProperty(value = "市code")
    private String cityCode;

    /**
     * 区code
     */
    @ApiModelProperty(value = "区code")
    private String regionCode;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}