package com.yiling.admin.b2b.strategy.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

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
public class DeleteStrategyPlatformGoodsLimitForm extends BaseForm {

    @ApiModelProperty("营销活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long marketingStrategyId;

    @ApiModelProperty("规格ID-添加时使用")
    private Long sellSpecificationsId;

    @ApiModelProperty("规格ID-添加当前页时使用")
    private List<Long> sellSpecificationsIdList;

    @ApiModelProperty("商品ID-精确搜索")
    private Long standardIdPage;

    @ApiModelProperty("规格ID-精确搜索")
    private Long sellSpecificationsIdPage;

    @ApiModelProperty("商品名称-模糊搜索")
    private String goodsNamePage;

    @ApiModelProperty("生产厂家-模糊搜索")
    private String manufacturerPage;

    @ApiModelProperty("以岭品 0-全部 1-是 2-否")
    private Integer isYiLing;
}
