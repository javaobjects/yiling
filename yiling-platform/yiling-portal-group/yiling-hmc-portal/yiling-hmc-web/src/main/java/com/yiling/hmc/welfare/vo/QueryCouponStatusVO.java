package com.yiling.hmc.welfare.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 查询福利券状态对象
 *
 * @author fan.shen
 * @date 2022-10-10
 */
@Data
@Accessors(chain = true)
public class QueryCouponStatusVO {

    private static final long serialVersionUID = 1L;

    /**
     * 入组药店
     */
    @ApiModelProperty("1-未被核销, 2-已被核销，3-已被核销最后一张")
    private Integer flag;

}
