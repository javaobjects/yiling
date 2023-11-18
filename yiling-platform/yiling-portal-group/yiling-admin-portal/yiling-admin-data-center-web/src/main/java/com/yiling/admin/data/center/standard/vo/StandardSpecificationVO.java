package com.yiling.admin.data.center.standard.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 基础药品选择包装规格后包装规格信息
 * @author:wei.wang
 * @date:2021/8/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardSpecificationVO extends BaseVO {

    /**
     *标准库ID
     */
    @ApiModelProperty(value = "标准库ID")
    private Long standardId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String sellSpecifications;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 条形码
     */
    @ApiModelProperty(value = "条形码")
    private String barcode;
}
