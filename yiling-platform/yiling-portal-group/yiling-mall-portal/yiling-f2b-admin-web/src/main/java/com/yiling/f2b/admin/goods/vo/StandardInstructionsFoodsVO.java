package com.yiling.f2b.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsFoodsVO extends BaseVO {

    /**
     * 配料
     */
    @ApiModelProperty(value = "配料")
    private String ingredients;

    /**
     * 保质期
     */
    @ApiModelProperty(value = "保质期")
    private String expirationDate;

    /**
     * 储藏
     */
    @ApiModelProperty(value = "储藏")
    private String store;

    /**
     * 致敏源信息
     */
    @ApiModelProperty(value = "致敏源信息")
    private String allergens;


}
