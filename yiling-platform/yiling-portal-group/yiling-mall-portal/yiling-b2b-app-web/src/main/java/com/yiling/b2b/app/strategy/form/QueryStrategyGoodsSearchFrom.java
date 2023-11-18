package com.yiling.b2b.app.strategy.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/9/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyGoodsSearchFrom extends QueryPageListForm {

    @NotNull
    @ApiModelProperty(value = "策略满赠活动id")
    private Long strategyId;
    
    @ApiModelProperty(value = "搜索词")
    private String key;
}
