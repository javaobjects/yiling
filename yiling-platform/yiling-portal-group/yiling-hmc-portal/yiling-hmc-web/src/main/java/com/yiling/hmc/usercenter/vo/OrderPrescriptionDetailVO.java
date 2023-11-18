package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 订单处方详情VO
 * </p>
 *
 * @author fan.shen
 * @date 2022/4/13
 */
@Data
public class OrderPrescriptionDetailVO extends BaseVO {

    @ApiModelProperty("处方信息")
    private OrderPrescriptionVO prescription;

    @ApiModelProperty("处方商品")
    private List<OrderPrescriptionGoodsVO> goodsList;


}
