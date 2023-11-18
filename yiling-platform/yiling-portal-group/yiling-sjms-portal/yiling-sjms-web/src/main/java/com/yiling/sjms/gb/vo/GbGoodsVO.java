package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 团购商品
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Data
public class GbGoodsVO extends BaseVO {


    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private Long code;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String name;

    /**
     * 产品规格
     */
    @ApiModelProperty(value = "产品规格")
    private String specification;

    /**
     * 小包装
     */
    @ApiModelProperty(value = "小包装")
    private Integer smallPackage;

    /**
     * 产品单价
     */
    @ApiModelProperty(value = "产品单价")
    private BigDecimal price;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

}
