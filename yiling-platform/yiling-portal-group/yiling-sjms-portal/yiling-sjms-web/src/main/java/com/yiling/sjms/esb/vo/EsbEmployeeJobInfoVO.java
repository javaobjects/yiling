package com.yiling.sjms.esb.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2023/5/24
 */
@Data
public class EsbEmployeeJobInfoVO {

    @ApiModelProperty("工号")
    private String empId;

    @ApiModelProperty("姓名")
    private String empName;

    @ApiModelProperty("岗位名称")
    private String jobName;

    @ApiModelProperty("所属部门岗位ID")
    private Long jobId;

    @ApiModelProperty("上级工号")
    private String superior;

    @ApiModelProperty("上级工号")
    private String superiorName;

    @ApiModelProperty("上级岗位名称")
    private String superiorJobName;

    @ApiModelProperty("上级岗位id")
    private Long superiorJobId;

    @ApiModelProperty("营销省区名称")
    private String yxProvince;

    @ApiModelProperty("营销部门名称")
    private String yxDept;

    @ApiModelProperty("营销区办名称")
    private String yxArea;
}
