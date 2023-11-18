package com.yiling.b2b.app.promotion.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shixing.sun
 * @date 2022-05-06
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class CombinationPackageVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "促销活动ID")
    private Long promotionActivityId;

    @ApiModelProperty(value = "活动名称")
    private String promotionName;

    @ApiModelProperty(value = "活动类型（1-满赠；2-特价；3-秒杀；4-组合包）")
    private Integer type;

    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    @ApiModelProperty(value = "费用承担方（1-平台；2-商家）")
    private Integer bear;

    @ApiModelProperty(value = "分摊-平台百分比")
    private BigDecimal platformPercent;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "结束时间")
    private String packageName;

    @ApiModelProperty(value = "组合包起购数量")
    private Integer initialNum;

    @ApiModelProperty(value = "退货要求")
    private String returnRequirement;

    @ApiModelProperty(value = "组合包商品简称")
    private String packageShortName;

    /**
     * 总数量
     */
    @ApiModelProperty(value = "总数量")
    private Integer totalNum;

    /**
     * 每人最大数量
     */
    @ApiModelProperty(value = "每人最大数量")
    private Integer perPersonNum;

    /**
     * 每人每天数量
     */
    @ApiModelProperty(value = "每人每天数量")
    private Integer perDayNum;

    @ApiModelProperty(value = "活动图片")
    private String pic;

    @ApiModelProperty(value = "组合包与其他营销活动说明")
    private String descriptionOfOtherActivity;

    @ApiModelProperty(value = "组合包关联的产品")
    private List<PromotionGoodsLimitDTO> goodsLimitDTOS;

    @ApiModelProperty("组合包状态：状态：1-正常 2-未建采")
    private Integer status;

    private List<Long> goodsIds;

    /**
     * 允许购买数量
     */
    private List<Integer>  allowBuyCount;

    /**
     * 组合包商品总价
     */
    private BigDecimal combinationPackagePrice;

    /**
     * 实际销售价格
     */
    private BigDecimal sellingPrice;

    /**
     * 组合包立省价格
     */
    private BigDecimal combinationDiscountPrice;

    /**
     * 是否有货
     */
    private Integer inStock;

    /**
     * 组合包最大购买套数
     */
    private Integer maxBuyNum;

    /**
     * 组合包剩余最大购买套数
     */
    private Integer surplusBuyNum;

    List<GoodsSkuDTO> goodsSkuDTOS;

    /**
     * 限购信息
     */
    @ApiModelProperty(value = "是否达到每人总量或者每人每天总量限购")
    private Boolean reachLimit=false;
}
