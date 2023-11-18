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
public class QueryStrategyEnterpriseGoodsLimitPageForm extends QueryPageListForm {

    @ApiModelProperty("营销活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long marketingStrategyId;
    
    @ApiModelProperty("商家范围类型（1-全部商家；2-指定商家；）")
    @NotNull(message = "未选中商家范围类型")
    private Integer conditionSellerType;

    @ApiModelProperty("商品ID-精确搜索")
    private Long goodsId;

    @ApiModelProperty("商品名称-模糊搜索")
    private String goodsName;

    @ApiModelProperty("企业名称-模糊搜索")
    private String ename;

    @ApiModelProperty("以岭品 0-全部 1-是 2-否")
    private Integer yilingGoodsFlag;

    @ApiModelProperty("商品状态：1上架 2下架 3待设置")
    private Integer goodsStatus;
}
