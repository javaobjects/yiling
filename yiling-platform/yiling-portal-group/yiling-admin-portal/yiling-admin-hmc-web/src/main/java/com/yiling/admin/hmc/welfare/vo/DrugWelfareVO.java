package com.yiling.admin.hmc.welfare.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: benben.jia
 * @data: 2022/09/26
 */
@Data
public class DrugWelfareVO extends BaseVO {

    /**
     * 用药福利计划名称
     */
    @ApiModelProperty(value = "用药福利计划名称")
    private String name;

    /**
     * 药品
     */
    @ApiModelProperty(value = "药品")
    private String drugSellSpecifications;

    /**
     * 活动周期_开始时间
     */
    @ApiModelProperty(value = "活动周期_开始时间")
    private Date beginTime;

    /**
     * 活动周期_结束时间
     */
    @ApiModelProperty(value = "活动周期_结束时间")
    private Date endTime;

    /**
     * 活动状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "活动状态：1-启用 2-停用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
