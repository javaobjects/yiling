package com.yiling.sales.assistant.app.userteam.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 我是队长时个人信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@ApiModel
@Accessors(chain = true)
public class MyLeaderVO {

    /**
     * 团队名称
     */
    @ApiModelProperty("团队名称")
    private String teamName;

    /**
     * 队员数量
     */
    @ApiModelProperty("队员数量")
    private Integer memberNum;

    /**
     * 团队总单量
     */
    @ApiModelProperty("团队总单量")
    private Integer sumOrderNum;

    /**
     * 团队订单总金额
     */
    @ApiModelProperty("团队订单总金额")
    private BigDecimal sumOrderAmount;

    /**
     * 组队时间
     */
    @ApiModelProperty("组队时间")
    private Date createTime;


}
