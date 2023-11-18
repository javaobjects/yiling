package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动商品表
 * </p>
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionGoodsLimitDTO extends BaseDTO {

    private static final long                serialVersionUID = 1L;

    /**
     * 促销活动ID
     */
    private Long                             promotionActivityId;

    /**
     * 活动名称
     */
    private String                           promotionName;

    /**
     * 活动类型（1-满赠；2-特价；3-秒杀；4-组合包）
     */
    private Integer                          type;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer                          sponsorType;

    /**
     * 费用承担方（1-平台；2-商家）
     */
    private Integer                          bear;

    /**
     * 分摊-平台百分比
     */
    private BigDecimal                       platformPercent;

    /**
     * 开始时间
     */
    private Date                             beginTime;

    /**
     * 结束时间
     */
    private Date                             endTime;

    /**
     * 商品所属企业ID
     */
    private Long                             eid;

    /**
     * 商品所属企业名称
     */
    private String                           ename;

    /**
     * 商品ID
     */
    private Long                             goodsId;

    /**
     * 销售价格
     */
    private BigDecimal                       price;

    /**
     * 活动价格
     */
    private BigDecimal                       promotionPrice;

    /**
     * 允许购买数量
     */
    private Integer                          allowBuyCount;

    /**
     * 剩余可购数量
     */
    private Integer                          leftBuyCount;

    /**
     * 关联赠品(只有满赠有)
     */
    private List<PromotionGoodsGiftLimitDTO> goodsGiftLimitList;

    /**
     * 组合包单个商品总价
     */
    private BigDecimal packageTotalPrice;

    /**
     * 商品关联skuid
     */
    private Long goodsSkuId;

    /**
     * 组合包名称
     */
    private String packageName;

    /**
     * 组合包起购数量
     */
    private Integer initialNum;

    /**
     * 退货要求
     */
    private String returnRequirement;

    /**
     * 组合包商品简称
     */
    private String packageShortName;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 每人最大数量
     */
    private Integer perPersonNum;

    /**
     * 每人每天数量
     */
    private Integer perDayNum;

    /**
     * 活动图片
     */
    private String pic;

    /**
     * 组合包与其他营销活动说明
     */
    private String descriptionOfOtherActivity;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    private String platformSelected;

    /**
     * 组合包专用
     */
    private List<PromotionGoodsLimitDTO> goodsLimitDTOS;

    /**
     * 组合包专用
     */
    private PromotionSecKillSpecialDTO secKillSpecialDTO;

    /**
     * 组合包最大购买套数
     */
    private Integer maxBuyNum;
}