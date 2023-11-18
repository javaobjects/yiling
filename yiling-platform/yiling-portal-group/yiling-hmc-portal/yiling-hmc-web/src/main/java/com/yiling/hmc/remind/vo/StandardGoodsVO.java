package com.yiling.hmc.remind.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author: fan.shen
 * @date: 2022/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardGoodsVO extends BaseVO {

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "注册证号")
    private String licenseNo;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String picUrl;

    /**
     * 适应症
     */
    @ApiModelProperty(value = "适应症")
    private String indications;

    /**
     * 用法与用量
     */
    @ApiModelProperty(value = "用法与用量")
    private String usageDosage;



}
