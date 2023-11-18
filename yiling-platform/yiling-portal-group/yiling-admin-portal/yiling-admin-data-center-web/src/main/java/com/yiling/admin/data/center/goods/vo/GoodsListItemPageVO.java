package com.yiling.admin.data.center.goods.vo;

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
     * 待审核
     */
    @ApiModelProperty(value = "待审核")
    private Long underReviewCount;

    /**
     * 驳回
     */
    @ApiModelProperty(value = "驳回")
    private Long rejectCount;
}
