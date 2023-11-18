package com.yiling.open.cms.question.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标准库商品名称和规格
 */
@Data
public class QuestionStandardGoodsInfoVO implements java.io.Serializable {

    /**
     *  标准库药品名称
     */
    @ApiModelProperty("标准库药品名称")
    private String name;

    // /**
    //  * 规格
    //  */
    // @ApiModelProperty("规格")
    // private String sellSpecifications;
}
