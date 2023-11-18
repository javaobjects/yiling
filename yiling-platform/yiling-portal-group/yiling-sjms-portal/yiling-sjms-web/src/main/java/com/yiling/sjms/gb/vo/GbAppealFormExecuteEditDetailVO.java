package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
public class GbAppealFormExecuteEditDetailVO {

    /**
     * 团购数据处理扣减/增加ID
     */
    @ApiModelProperty(value = "团购数据处理扣减/增加ID")
    private Long id;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    /**
     * 机构部门ID(用户自己填写部门名称时此id为0)
     */
    @ApiModelProperty(value = "部门ID")
    private Long orgId;

    /**
     * 机构部门
     */
    @ApiModelProperty(value = "部门")
    private String department;

    /**
     * 部门全路径
     */
    @ApiModelProperty(value = "部门全路径")
    private String fullpath;

    /**
     * 机构业务部门ID(用户自己填写业务部门名称时此id为0)
     */
    @ApiModelProperty(value = "业务部门ID")
    private Long businessOrgId;

    /**
     * 机构业务部门
     */
    @ApiModelProperty(value = "业务部门")
    private String businessDepartment;

    /**
     * 业务部门全路径
     */
    @ApiModelProperty(value = "业务部门全路径")
    private String businessFullpath;

    /**
     * 机构省区
     */
    @ApiModelProperty(value = "省区")
    private String provincialArea;

    /**
     * 机构业务省区
     */
    @ApiModelProperty(value = "业务省区")
    private String businessProvince;

    /**
     * 机构区办代码
     */
    @ApiModelProperty(value = "业务区域代码")
    private String districtCountyCode;

    /**
     * 机构区办
     */
    @ApiModelProperty(value = "业务区域")
    private String districtCounty;

    /**
     * 主管工号
     */
    @ApiModelProperty(value = "上级主管工号")
    private String superiorSupervisorCode;

    /**
     * 主管名称
     */
    @ApiModelProperty(value = "上级主管名称")
    private String superiorSupervisorName;

    /**
     * 代表工号
     */
    @ApiModelProperty(value = "业务代表工号")
    private String representativeCode;

    /**
     * 代表姓名
     */
    @ApiModelProperty(value = "业务代表姓名")
    private String representativeName;

    /**
     * 岗位代码
     */
    @ApiModelProperty(value = "岗位代码")
    private Long postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postName;

    /**
     * 分配类型：1-扣减 2-增加
     */
    @ApiModelProperty(value = "分配类型：1-扣减 2-增加")
    private Integer allocationType;

}
