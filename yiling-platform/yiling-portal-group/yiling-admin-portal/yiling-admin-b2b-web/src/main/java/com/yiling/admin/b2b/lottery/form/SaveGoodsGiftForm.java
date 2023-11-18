package com.yiling.admin.b2b.lottery.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存赠品库信息
 * @author:wei.wang
 * @date:2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGoodsGiftForm extends BaseForm {


    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    @NotBlank(message = "不能为空")
    private String name;

    /**
     * 商品类别（1-真实物品；2-虚拟物品；3-优惠券；4-会员）
     */
    @ApiModelProperty("商品类别：1-真实物品；2-虚拟物品；3-优惠券；4-会员")
    @NotNull(message = "不能为空")
    private Integer goodsType;

    /**
     * 所属业务（1-全部；2-2b；3-2c
     */
    @ApiModelProperty("所属业务（1-全部；2-2b；3-2c")
    @NotNull(message = "不能为空")
    private Integer businessType;

    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    @NotNull(message = "不能为空")
    private Long quantity;

    /**
     * 商品安全库存数量
     */
    @ApiModelProperty("商品安全库存数量")
    @NotNull(message = "不能为空")
    private Long safeQuantity;

    /**
     * 商品规格
     */
    @ApiModelProperty("商品规格")
    private String specifications;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
     * 商品品牌
     */
    @ApiModelProperty("商品品牌")
    private String brand;

    /**
     * 商品单价
     */
    @ApiModelProperty("商品单价")
    private BigDecimal price;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String pictureUrl;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片key")
    @NotBlank(message = "不能为空")
    private String pictureKey;

    /**
     *卡号信息
     */
    @ApiModelProperty("卡号信息")
    private List<CardForm> cardList;

    /**
     * 会员id
     */
    @ApiModelProperty("会员id")
    private Long memberId;

    /**
     * 优惠劵活动id
     */
    @ApiModelProperty("优惠劵活动id")
    private Long couponActivityId;

    /**
     * 说明内容
     */
    @ApiModelProperty("说明内容")
    private String introduction;

}

