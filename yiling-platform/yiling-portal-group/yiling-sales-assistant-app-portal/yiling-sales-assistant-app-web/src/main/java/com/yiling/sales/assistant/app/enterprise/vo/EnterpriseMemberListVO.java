package com.yiling.sales.assistant.app.enterprise.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 企业成员列表 VO
 *
 * @author: lun.yu
 * @date: 2021/9/23
 */
@Builder
@Data
public class EnterpriseMemberListVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("上级姓名")
    private String parentName;

    @ApiModelProperty("已售商品数量")
    private Integer productNum;

    @ApiModelProperty("订单总额")
    private BigDecimal orderAmount;
}
