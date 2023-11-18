package com.yiling.sjms.wash.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockFlowWashSalePageVO<T> extends Page<T> {

    @ApiModelProperty(value = "已分配")
    private Integer hasDistributionCount;
    @ApiModelProperty(value = "未分配")
    private Integer notDistributionCount;
}
