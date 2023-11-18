package com.yiling.admin.b2b.lottery.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsGiftVO extends BaseVO {


    /**
     * 类型（1-平台；2-商家）
     */
    @ApiModelProperty("类型:1-平台 2-商家")
    private Integer sponsorType;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 商品类别（1-真实物品；2-虚拟物品；3-优惠券；4-会员）
     */
    @ApiModelProperty("商品类别：1-真实物品 2-虚拟物品 3-优惠券 4-会员")
    private Integer goodsType;

    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private Long quantity;

    /**
     * 商品安全库存数量
     */
    @ApiModelProperty("商品安全库存数量")
    private Long safeQuantity;

    /**
     * 所属业务（1-全部；2-2b；3-2c
     */
    @ApiModelProperty("所属业务（1-全部；2-2b；3-2c")
    private Integer businessType;

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
     * 卡号信息
     */
    @ApiModelProperty("卡号信息")
    private List<CardVO> cardList;

    /**
     * 图片对象
     */
    @ApiModelProperty("图片对象")
    private FileInfoVO fileInfo;

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
