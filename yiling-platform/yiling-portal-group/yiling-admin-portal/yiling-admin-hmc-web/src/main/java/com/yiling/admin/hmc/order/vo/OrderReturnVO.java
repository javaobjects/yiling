package com.yiling.admin.hmc.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 退货表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
public class OrderReturnVO extends BaseVO {

    @ApiModelProperty("退货单编号")
    private String returnNo;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("第三方退单编号")
    private String thirdReturnNo;

    @ApiModelProperty("药品服务终端id")
    private Long eid;

    @ApiModelProperty("药品服务终端名称")
    private String ename;

    @ApiModelProperty("状态：1-待审核/2-已退/3-已取消退单")
    private Integer returnStatus;

    @ApiModelProperty("申请退款金额")
    private BigDecimal returnAmount;

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


}
