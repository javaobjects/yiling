package com.yiling.hmc.welfare.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 入组对象
 *
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@Accessors(chain = true)
public class GroupWelfareVO {

    private static final long serialVersionUID = 1L;

    /**
     * 入组药店
     */
    @ApiModelProperty("入组药店")
    private String eName;

    /**
     * 用药人姓名
     */
    @ApiModelProperty("用药人姓名")
    private String medicineUserName;

    /**
     * 用药人手机号
     */
    @ApiModelProperty("用药人手机号")
    private String medicineUserPhone;

    /**
     * 组id
     */
    @ApiModelProperty("组id")
    private Long groupId;

    /**
     * 药品福利计划名称
     */
    @ApiModelProperty("药品福利计划名称")
    private String name;

    /**
     * 服用药物
     */
    @ApiModelProperty("服用药物")
    private String drugName;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 入组id
     */
    @ApiModelProperty(value = "入组id")
    private Long joinGroupId;

    /**
     * 入组福利券包
     */
    @ApiModelProperty("入组福利券包")
    private List<GroupCouponVO> groupCouponVOList;


}
