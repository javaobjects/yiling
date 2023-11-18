package com.yiling.admin.b2b.integral.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-添加平台SKU Form
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIntegralGivePlatformGoodsForm extends BaseForm {

    /**
     * 发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

    /**
     * 规格ID-添加时使用
     */
    @ApiModelProperty("规格ID-添加时使用")
    private Long sellSpecificationsId;

    /**
     * 规格ID-添加当前页时使用
     */
    @ApiModelProperty("规格ID-添加当前页时使用")
    private List<Long> sellSpecificationsIdList;

    /**
     * 商品ID-精确搜索
     */
    @ApiModelProperty("商品ID-精确搜索")
    private Long standardIdPage;

    /**
     * 规格ID-精确搜索
     */
    @ApiModelProperty("规格ID-精确搜索")
    private Long sellSpecificationsIdPage;

    /**
     * 商品名称-模糊搜索
     */
    @ApiModelProperty("商品名称-模糊搜索")
    private String goodsNamePage;

    /**
     * 生产厂家-模糊搜索
     */
    @ApiModelProperty("生产厂家-模糊搜索")
    private String manufacturerPage;

    /**
     * 以岭品 0-全部 1-是 2-否
     */
    @ApiModelProperty("以岭品 0-全部 1-是 2-否")
    private Integer isYiLing;
}
