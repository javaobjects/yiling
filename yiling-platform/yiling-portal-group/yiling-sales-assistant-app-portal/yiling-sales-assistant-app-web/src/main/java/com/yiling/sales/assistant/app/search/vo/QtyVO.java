package com.yiling.sales.assistant.app.search.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QtyVO extends BaseVO {

    @ApiModelProperty("可用库存数量")
    private Long usableInventory;

    @ApiModelProperty("可用状态 true可用false不可用")
    private Boolean usableBool;

}
