package com.yiling.sjms.crm.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 BaseCrmGoodsInfoVO
 * @描述 基础商品信息
 * @创建时间 2023/4/11
 * @修改人 shichen
 * @修改时间 2023/4/11
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BaseCrmGoodsInfoVO extends BaseVO {

    @ApiModelProperty(value = "商品编码")
    private Long goodsCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String goodsName;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String goodsSpec;

    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private Long categoryId;

    /**
     * 品种类型
     */
    @ApiModelProperty(value = "品种类型")
    private String category;
}
