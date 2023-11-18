package com.yiling.b2b.app.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单快递信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralExchangeOrderExpressVO extends BaseVO {

    /**
     * 快递公司（见字典）
     */
    @ApiModelProperty("快递公司")
    private String expressCompany;

    /**
     * 快递单号
     */
    @ApiModelProperty("快递单号")
    private String expressOrderNo;

}
