package com.yiling.hmc.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 药品福利
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/25
 */
@Data
@Accessors(chain = true)
@Builder
@ApiModel("药品福利")
@AllArgsConstructor
@NoArgsConstructor
public class MedicineItemVO {

    @ApiModelProperty("eId")
    private Long eId;

    @ApiModelProperty("goodsId")
    private Long hmcGoodsId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("图片地址")
    private String picUrl;

    @ApiModelProperty("规格")
    private String specifications;

    @ApiModelProperty("市场价")
    private BigDecimal marketPrice;

    @ApiModelProperty("节省")
    private BigDecimal savePrice;

    @ApiModelProperty("参保地址")
    private String buyInsuranceUrl;

    @ApiModelProperty("标准库规格id")
    private Long sellSpecificationsId;

    @ApiModelProperty("保险id")
    private Long insuranceId;

}
