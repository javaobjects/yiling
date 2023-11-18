package com.yiling.b2b.app.member.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B移动端-会员购买记录 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberBuyRecordVO extends BaseVO {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号", hidden = true)
    private String orderNo;

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 展示名称
     */
    @ApiModelProperty("展示名称")
    private String stageName;

    /**
     * 会员开始时间
     */
    @ApiModelProperty("会员开始时间")
    private Date startTime;

    /**
     * 会员结束时间
     */
    @ApiModelProperty("会员结束时间")
    private Date endTime;

    /**
     * 有效天数
     */
    @ApiModelProperty("有效天数")
    private Integer validDays;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;

    /**
     * 会员购买时间
     */
    @ApiModelProperty("会员购买时间")
    private Date createTime;

}
