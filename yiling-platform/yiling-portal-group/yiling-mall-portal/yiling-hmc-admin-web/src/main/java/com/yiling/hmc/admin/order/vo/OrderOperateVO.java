package com.yiling.hmc.admin.order.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单操作表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-04-21
 */
@Data
public class OrderOperateVO extends BaseVO {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("操作人")
    private Long operateUserId;

    @ApiModelProperty("操作时间")
    private Date operateTime;

    @ApiModelProperty("操作功能:1-自提/2-发货/3-退货/4-收货")
    private Integer operateType;

    @ApiModelProperty("内容日志")
    private String content;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;

    // =======================================

    @ApiModelProperty("操作人名称")
    private String operateUserName;
}
