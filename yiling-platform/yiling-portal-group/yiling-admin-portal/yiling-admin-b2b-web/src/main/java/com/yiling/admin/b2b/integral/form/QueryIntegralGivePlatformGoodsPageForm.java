package com.yiling.admin.b2b.integral.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-平台SKU接口分页列表查询 Form
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralGivePlatformGoodsPageForm extends QueryPageListForm {

    @ApiModelProperty("发放规则ID")
    private Long giveRuleId;

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
