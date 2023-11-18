package com.yiling.admin.hmc.insurance.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CashGoodsVO extends BaseVO {



    /**
     * 药品名称
     */
    @ApiModelProperty(value = "药品名称")
    private String goodsName;


    /**
     * 药品规格
     */
    @ApiModelProperty("规格")
    private String goodsSpecifications;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;


}
