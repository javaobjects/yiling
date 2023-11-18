package com.yiling.sjms.gb.vo;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 *
 * @author: wei.wang
 * @date: 2022/11/30
 */
@Data

public class GbEmployeeDTOVO extends BaseDTO {


    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String empName;

    /**
     * 工号
     */
    @ApiModelProperty(value = "工号")
    private String empId;

    /**
     * 所属部门ID
     */
    @ApiModelProperty(value = "所属部门名区办Id")
    private Long deptId;


    /**
     * 所属部门名称
     */
    @ApiModelProperty(value = "所属部门名区办")
    private String deptName;

    /**
     * 所属部门岗位名称
     */
    @ApiModelProperty(value = "所属部门岗位名称")
    private String jobName;


    /**
     * 营销部门名称
     */
    @ApiModelProperty(value = "营销部门名称")
    private String yxDept;

    /**
     * 是否市场运营部
     */
    @ApiModelProperty(value = "是否市场运营部 true-是,false -否")
    private Boolean  marketFlag;


    /**
     * 营销省区名称
     */
    @ApiModelProperty(value = "省区")
    private String yxProvince;


    /**
     * 营销区办名称
     */
    @ApiModelProperty(value = "区办")
    private String yxArea;

}
