package com.yiling.admin.b2b.goods.vo;

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
public class StandardGoodsPicVO extends BaseVO {

    /**
     * 商品图片值
     */
    @ApiModelProperty(value = "商品图片值")
    private String pic;

    /**
     * 商品图片url
     */
    @ApiModelProperty(value = "商品图片url")
    private String picUrl;

    /**
     * 图片排序
     */
    @ApiModelProperty(value = "图片排序")
    private Integer picOrder;

    /**
     * 是否商品默认图片（0否1是）
     */
    @ApiModelProperty(value = "是否商品默认图片（0否1是）")
    private Integer picDefault;

}
