package com.yiling.b2b.admin.goods.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/16
 */
@Data
public class GoodsListItemPageVO<T> extends Page {
    /**
     * 上架
     */
    @ApiModelProperty(value = "上架")
    private Long upShelfCount;

    /**
     * 下架
     */
    @ApiModelProperty(value = "下架")
    private Long unShelfCount;

    /**
     * 待审核
     */
    @ApiModelProperty(value = "待设置")
    private Long waitSetCount;
}
