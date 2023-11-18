package com.yiling.admin.b2b.integral.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换商品详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Data
@Accessors(chain = true)
public class IntegralExchangeGoodsDetailVO extends BaseVO {

    /**
     * 物品ID
     */
    @ApiModelProperty("物品ID")
    private Long goodsId;

    /**
     * 物品名称
     */
    @ApiModelProperty("物品名称")
    private String goodsName;

    /**
     * 价值/面额（赠品库为价值，优惠券为面额）
     */
    @ApiModelProperty("价值/面额（赠品库为价值，优惠券为面额）")
    private BigDecimal price;

    /**
     * 可用库存
     */
    @ApiModelProperty("可用库存")
    private Long availableQuantity;

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @ApiModelProperty("商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券")
    private Integer goodsType;

    /**
     * 兑换所需积分
     */
    @ApiModelProperty("兑换所需积分")
    private Integer exchangeUseIntegral;

    /**
     * 可兑换数量
     */
    @ApiModelProperty("可兑换数量")
    private Integer canExchangeNum;

    /**
     * 单品兑换限制（份/用户）
     */
    @ApiModelProperty("单品兑换限制（份/用户）")
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
    @ApiModelProperty("是否区分用户身份：1-全部 2-指定会员类型")
    private Integer userFlag;

    /**
     * 会员ID集合
     */
    @ApiModelProperty("会员ID集合")
    private List<Long> memberIdList;

    /**
     * 有效期生效时间
     */
    @ApiModelProperty("有效期生效时间")
    private Date validStartTime;

    /**
     * 有效期失效时间
     */
    @ApiModelProperty("有效期失效时间")
    private Date validEndTime;

    /**
     * 上架状态：1-已上架 2-已下架
     */
    @ApiModelProperty("上架状态：1-已上架 2-已下架")
    private Integer shelfStatus;

    /**
     * 排序值
     */
    @ApiModelProperty("排序值")
    private Integer sort;

}
