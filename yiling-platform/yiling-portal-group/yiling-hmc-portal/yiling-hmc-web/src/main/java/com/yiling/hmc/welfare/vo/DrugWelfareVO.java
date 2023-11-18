package com.yiling.hmc.welfare.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@Accessors(chain = true)
public class DrugWelfareVO {

    private static final long serialVersionUID = 1L;

    /**
     * 福利计划id
     */
    @ApiModelProperty(value = "福利计划id")
    private Long welfareId;

    /**
     * 员工企业id
     */
    @ApiModelProperty(value = "企业id")
    private Long sellerEId;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private Long sellerUserId;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String name;

    /**
     * 药店名称
     */
    @ApiModelProperty("药店名称")
    private String eName;

    /**
     * 药店名称
     */
    @ApiModelProperty("节省盒数")
    private Long saveCount;

    /**
     * 药品规格id
     */
    @ApiModelProperty("药品规格id")
    private Long sellSpecificationsId;

    /**
     * 服用药物
     */
    @ApiModelProperty("服用药物")
    private String drugName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 活动状态：1-启用 2-停用
     */
    @ApiModelProperty("活动状态：1-启用 2-停用")
    private Integer status;

    /**
     * 药品福利券包
     */
    @ApiModelProperty("药品福利券包")
    private List<DrugWelfareCouponVO> couponVOList;

}
