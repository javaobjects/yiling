package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmManorRepresentativeVO extends BaseVO {

    private Long manorId;
    /**
     * 辖区编码
     */
    @ApiModelProperty(value = "辖区编码")
    private String manorNo;

    /**
     * 辖区名称
     */
    @ApiModelProperty(value = "辖区名称")
    private String name;
    /**
     * 代表岗位编码
     */
    @ApiModelProperty(value = "岗位编码")
    private Long representativePostCode;

    /**
     * 代表岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String representativePostName;
    /**
     * 部门
     */
    @ApiModelProperty("部门")
    private String department;

    /**
     * 业务部门
     */
    @ApiModelProperty("业务部门")
    private String businessDepartment;

    /**
     * 省区
     */
    @ApiModelProperty("省区")
    private String provincialArea;

    /**
     * 业务省区
     */
    @ApiModelProperty("业务省区")
    private String businessProvince;

    /**
     * 业务区域代码
     */
    @ApiModelProperty("业务区域代码")
    private String businessAreaCode;

    /**
     * 业务区域
     */
    @ApiModelProperty("业务区域")
    private String businessArea;

    /**
     * 业务区域描述
     */
    @ApiModelProperty("业务区域描述")
    private String businessAreaDescription;

    /**
     * 上级主管编码
     */
    @ApiModelProperty("上级主管编码")
    private String superiorSupervisorCode;

    /**
     * 上级主管名称
     */
    @ApiModelProperty("上级主管名称")
    private String superiorSupervisorName;

    /**
     * 代表编码
     */
    @ApiModelProperty("代表编码")
    private String representativeCode;

    /**
     * 代表名称
     */
    @ApiModelProperty("代表名称")
    private String representativeName;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "操作人ID")
    private Long updateUser;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "操作人姓名")
    private String updateUserName;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "操作人时间")
    private Date updateTime;

    private String remark;
    /**
     * 上级主管岗位代码
     */
    @ApiModelProperty("上级主管岗位代码")
    private Long superiorJob;

    /**
     * 上级主管岗位名称
     */
    @ApiModelProperty("上级主管岗位名称")
    private String superiorJobName;

    /**
     * 职级编码
     */
    @ApiModelProperty("职级编码")
    private String dutyGredeId;
    /**
     * 省区经理岗位代码
     */
    @ApiModelProperty("省区经理岗位代码")
    private Long provincialManagerPostCode;

    /**
     * 省区经理岗位名称
     */
    @ApiModelProperty("省区经理岗位名称")
    private String provincialManagerPostName;

    /**
     * 省区经理工号
     */
    @ApiModelProperty("省区经理工号")
    private String provincialManagerCode;

    /**
     * 省区经理姓名
     */
    @ApiModelProperty("省区经理姓名")
    private String provincialManagerName;
}

