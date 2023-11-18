package com.yiling.admin.b2b.strategy.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyPlatformGoodsLimitPageForm extends QueryPageListForm {

    @ApiModelProperty("营销活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long marketingStrategyId;

    @ApiModelProperty("商品ID-精确搜索")
    private Long standardId;

    @ApiModelProperty("规格ID-精确搜索")
    private Long sellSpecificationsId;

    @ApiModelProperty("商品名称-模糊搜索")
    private String goodsName;

    @ApiModelProperty("生产厂家-模糊搜索")
    private String manufacturer;

    @ApiModelProperty("以岭品 0-全部 1-是 2-否")
    private Integer isYiLing;
}
