package com.yiling.b2b.app.integral.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分发放/扣减记录 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-30
 */
@Data
@Accessors(chain = true)
public class IntegralGiveUseRecordVO extends BaseVO {

    /**
     * 积分值
     */
    @ApiModelProperty("积分值")
    private Integer integralValue;

    /**
     * 发放/扣减时间
     */
    @ApiModelProperty("发放/扣减时间")
    private Date operTime;

    /**
     * 行为名称
     */
    @ApiModelProperty("行为名称")
    private String behaviorName;

    /**
     * 类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废
     */
    @ApiModelProperty("类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废")
    private Integer changeType;

    /**
     * 类型：1-发放 2-扣减
     */
    @ApiModelProperty("类型：1-发放 2-扣减")
    private Integer type;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 操作备注
     */
    @ApiModelProperty("操作备注")
    private String opRemark;


}
