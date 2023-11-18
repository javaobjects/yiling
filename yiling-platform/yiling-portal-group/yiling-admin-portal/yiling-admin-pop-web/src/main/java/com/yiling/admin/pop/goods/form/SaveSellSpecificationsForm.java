package com.yiling.admin.pop.goods.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Data
public class SaveSellSpecificationsForm {

    /**
     * 审核ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 标准库id
     */
    @ApiModelProperty(value = "标准库id")
    private Integer standardId;

    /**
     * 销售规格（关联rk_sell_specifications表id）
     */
    @ApiModelProperty(value = "销售规格ID")
    private Integer sellSpecificationsId;
}
