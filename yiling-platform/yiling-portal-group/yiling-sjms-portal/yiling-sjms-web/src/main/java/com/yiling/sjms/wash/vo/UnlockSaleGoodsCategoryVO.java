package com.yiling.sjms.wash.vo;

import com.yiling.sjms.crm.vo.CrmGoodsCategoryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/5/18
 */
@Data
public class UnlockSaleGoodsCategoryVO extends CrmGoodsCategoryVO {

    @ApiModelProperty(value = "非锁销量分配商品是否被占用 true不可用 false可用")
    private Boolean disable;

    @ApiModelProperty(value = "占用描述")
    private String disableDesc;
}
