package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;

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
public class SpecialAvtivityGoodsInfoDTO extends BaseDTO {

    /**
     * 供应商id，不是买家id
     */
    private Long eid;

    /**
     * 供应商id，不是买家id
     */
    private Long shopId;

    /**
     * 企业名称-供应商名称
     */
    private String ename;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 产品名称
     */
    private String packageName;

    /**
     * 组合包简称
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
     * 产品id
     */
    private Long goodsId;

    /**
     * 活动价格
     */
    private BigDecimal promotionPrice;

    /**
     * 原来价格
     */
    private BigDecimal price;

    /**
     * 最大购买数量
     */
    private Integer maxBuyCount;

    /**
     * 最小购买数量
     */
    private Integer initialNum;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 活动是否未开始 0开始 1未开始
     */
    private Boolean isStarted;

    /**
     * 是否建材
     */
    private Boolean isContract;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 图片
     */
    private String pic;

    /**
     * 允许购买数量
     */
    private Integer allowBuyCount;

    /**
     * 商品关联skuid
     */
    private Long goodsSkuId;

    /**
     * 产品skuid集合
     */
    private List<GoodsSkuDTO> goodsSkuList;

    /**
     * 产品goodsId集合
     */
    private List<SpecialAvtivityGoodsInfoDTO>goodsInfoDTOS;
}