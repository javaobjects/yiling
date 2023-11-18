package com.yiling.f2b.admin.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 可采购关系 VO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CanPurchaseRelationVO extends PurchaseRelationVO {

    /**
     * 是否已选择
     */
    @ApiModelProperty("采购关系是否已选择")
    private Boolean chooseFlag;

}
