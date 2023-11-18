package com.yiling.admin.hmc.welfare.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: benben.jia
 * @data: 2022/09/26
 */
@Data
public class DrugWelfareDetailVO extends BaseVO {

    /**
     * 用药福利计划名称
     */
    @ApiModelProperty(value = "用药福利计划名称")
    private String name;

    /**
     * 药品id
     */
    @ApiModelProperty(value = "药品id")
    private String sellSpecificationsId;

    /**
     * 药品
     */
    @ApiModelProperty(value = "药品")
    private String drugSellSpecifications;

    /**
     * 福利券包
     */
    @ApiModelProperty(value = "福利券包")
    private List<DrugWelfareCouponVO> drugWelfareCouponList;

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

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


}
