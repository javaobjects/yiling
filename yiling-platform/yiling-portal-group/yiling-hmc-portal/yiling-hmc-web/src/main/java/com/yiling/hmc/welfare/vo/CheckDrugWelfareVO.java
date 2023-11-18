package com.yiling.hmc.welfare.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@Accessors(chain = true)
public class CheckDrugWelfareVO {

    private static final long serialVersionUID = 1L;

    /**
     * 跳转标志位 0-不用跳，1-福利计划详情页，2-入组详情页
     */
    @ApiModelProperty("跳转标志位 0-不用跳，1-福利计划详情页，2-入组详情页")
    private Integer redirectFlag = 0;

    /**
     * 药品福利计划状态  0-正常，1-未开始，2-已过期，3-已结束
     */
    @ApiModelProperty("药品福利计划状态 0-正常，1-未开始，2-已过期，3-已结束")
    private Integer flag = 0;

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
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 入组id
     */
    @ApiModelProperty("入组id")
    private Long groupId;

    /**
     * 入组对象
     */
    @ApiModelProperty("已入其他组对象")
    private GroupWelfareVO otherGroupWelfareVO = new GroupWelfareVO();

}
