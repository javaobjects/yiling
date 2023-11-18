package com.yiling.admin.pop.category.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @description
 * @author lun.yu
 * @date 2021/8/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class CategoryGoodsDetailVO extends BaseVO {

    /**
     * 标准库ID
     */
    @ApiModelProperty(value = "标准库ID", example = "1111")
    private Long standardId;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "注册证号", example = "Z109090")
    private String licenseNo;

    /**
     * banner图片值
     */
    @ApiModelProperty(value = "banner图片值")
    private String pic;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 销售规格
     */
    @ApiModelProperty(value = "销售规格", example = "1片")
    private String sellSpecifications;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 挂网价
     */
    @ApiModelProperty(value = "挂网价")
    private BigDecimal price;

}
