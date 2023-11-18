package com.yiling.b2b.admin.order.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 赠品信息
 * @author:wei.wang
 * @date:2021/11/8
 */
@Data
public class OrderGiftVO extends BaseVO {
    /**
     * 类型（1-平台；2-商家）
     */
    @ApiModelProperty(value = "类型: 1-平台 2-商家")
    private Integer sponsorType;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String pictureUrl;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specifications;
}
