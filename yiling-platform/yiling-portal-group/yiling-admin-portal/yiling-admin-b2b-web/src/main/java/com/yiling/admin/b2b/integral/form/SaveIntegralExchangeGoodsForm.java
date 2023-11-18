package com.yiling.admin.b2b.integral.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分兑换商品 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralExchangeGoodsForm extends BaseForm {

    /**
     * ID（修改时必须传入）
     */
    @ApiModelProperty("ID（修改时必须传入）")
    private Long id;

    /**
     * 物品ID
     */
    @NotNull
    @ApiModelProperty(value = "物品ID（物品ID或优惠券ID）", required = true)
    private Long goodsId;

    /**
     * 物品名称
     */
    @NotEmpty
    @ApiModelProperty(value = "物品名称（物品名称或优惠券名称）", required = true)
    private String goodsName;

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @NotNull
    @ApiModelProperty(value = "商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券", required = true)
    private Integer goodsType;

    /**
     * 兑换所需积分
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "兑换所需积分", required = true)
    private Integer exchangeUseIntegral;

    /**
     * 可兑换数量
     */
    @NotNull
    @ApiModelProperty(value = "可兑换数量", required = true)
    private Integer canExchangeNum;

    /**
     * 单品兑换限制（份/用户）
     */
    @NotNull
    @ApiModelProperty(value = "单品兑换限制（份/用户）", required = true)
    private Integer singleMaxExchange;

    /**
     * 兑换限制生效时间
     */
    @ApiModelProperty("兑换限制生效时间")
    private Date exchangeStartTime;

    /**
     * 兑换限制失效时间
     */
    @ApiModelProperty("兑换限制失效时间")
    private Date exchangeEndTime;

    /**
     * 是否区分用户身份：1-全部 2-指定会员类型
     */
    @NotNull
    @ApiModelProperty(value = "是否区分用户身份：1-全部 2-指定会员类型", required = true)
    private Integer userFlag;

    /**
     * 会员ID集合
     */
    @ApiModelProperty("会员ID集合")
    private List<Long> memberIdList;

    /**
     * 有效期生效时间
     */
    @NotNull
    @ApiModelProperty(value = "有效期生效时间", required = true)
    private Date validStartTime;

    /**
     * 有效期失效时间
     */
    @NotNull
    @ApiModelProperty(value = "有效期失效时间", required = true)
    private Date validEndTime;

    /**
     * 排序值
     */
    @ApiModelProperty("排序值")
    private Integer sort;

}
