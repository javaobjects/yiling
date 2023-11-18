package com.yiling.sjms.wash.vo;

import com.yiling.sjms.crm.vo.CrmGoodsInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockSaleGoodsVO extends CrmGoodsInfoVO {

    @ApiModelProperty(value = "非锁销量分配商品是否被占用 true不可用 false可用")
    private Boolean disable;

    @ApiModelProperty(value = "占用描述")
    private String disableDesc;
}
