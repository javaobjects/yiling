package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-店铺SKU-已添加店铺SKU分页列表查询 Form
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralEnterpriseGoodsPageForm extends QueryPageListForm {

    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

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
