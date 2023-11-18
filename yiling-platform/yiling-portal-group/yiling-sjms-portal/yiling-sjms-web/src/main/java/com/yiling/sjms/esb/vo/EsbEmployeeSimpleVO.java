package com.yiling.sjms.esb.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ESB员工简单信息
 *
 * @author: xuan.zhou
 * @date: 2023/2/17
 */
@Data
public class EsbEmployeeSimpleVO {

    @ApiModelProperty("工号")
    private String empId;

    @ApiModelProperty("姓名")
    private String empName;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("岗位名称")
    private String jobName;

    @ApiModelProperty("所属部门岗位ID")
    private Long jobId;
}
