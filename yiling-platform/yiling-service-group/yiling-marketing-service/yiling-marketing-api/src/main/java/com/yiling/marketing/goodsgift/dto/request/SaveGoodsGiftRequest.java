package com.yiling.marketing.goodsgift.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveGoodsGiftRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 类型（1-平台；2-商家）
     */
    private Integer sponsorType;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品类别（1-真实物品；2-虚拟物品；3-优惠券；4-会员）
     */
    private Integer goodsType;

    /**
     * 商品数量
     */
    private Long quantity;

    /**
     * 商品安全库存数量
     */
    private Long safeQuantity;

    /**
     * 使用商品数量
     */
    private Long useQuantity;

    /**
     * 可用商品数量
     */
    private Long availableQuantity;

    /**
     * 所属业务（1-全部；2-2b；3-2c
     */
    private Integer businessType;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 商品图片
     */
    private String pictureUrl;

    /**
     * 商品图片key
     */
    private String pictureKey;

    /**
     * 卡号信息
     */
    List<CardRequest> cardList;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 优惠劵活动id
     */
    private Long couponActivityId;

    /**
     * 说明内容
     */
    private String introduction;

}
