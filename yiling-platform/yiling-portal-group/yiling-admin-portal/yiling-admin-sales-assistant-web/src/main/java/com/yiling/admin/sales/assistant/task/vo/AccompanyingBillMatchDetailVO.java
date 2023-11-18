package com.yiling.admin.sales.assistant.task.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 随货同行单匹配商品详情
 * </p>
 *
 * @author gxl
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AccompanyingBillMatchDetailVO extends BaseVO {

    /**
     * 以岭商品名称
     */
    @ApiModelProperty(value = "以岭商品名称")
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    @ApiModelProperty(value = "以岭商品名称")
    private String ylGoodsSpecifications;


    /**
     * 销售数量
     */
    @ApiModelProperty(value = "销售数量")
    private Long quantity;



}
