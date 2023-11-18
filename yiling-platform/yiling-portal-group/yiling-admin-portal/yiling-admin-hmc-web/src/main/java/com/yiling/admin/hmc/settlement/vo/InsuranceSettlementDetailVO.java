package com.yiling.admin.hmc.settlement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 保司结账明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-05-13
 */
@Data
public class InsuranceSettlementDetailVO extends BaseVO {

    @ApiModelProperty("保司结账主表id")
    private Long insuranceSettlementId;

    @ApiModelProperty("保险id")
    private Long insuranceRecordId;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("平台订单编号")
    private String orderNo;

    @ApiModelProperty("打款金额")
    private BigDecimal payAmount;

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
