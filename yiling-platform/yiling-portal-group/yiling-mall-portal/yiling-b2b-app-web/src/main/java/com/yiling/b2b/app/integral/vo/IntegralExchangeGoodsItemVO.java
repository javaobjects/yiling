package com.yiling.b2b.app.integral.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换商品列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@Accessors(chain = true)
public class IntegralExchangeGoodsItemVO extends BaseVO {

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
     * 商品图片地址
     */
    @ApiModelProperty("商品图片地址")
    private String pictureUrl;

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
     * 是否区分用户身份：1-全部 2-指定会员类型
     */
    @ApiModelProperty("是否区分用户身份：1-全部 2-指定会员类型")
    private Integer userFlag;

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
     * 排序值
     */
    @ApiModelProperty("排序值")
    private Integer sort;

    /**
     * 指定会员参加的标签
     */
    @ApiModelProperty("指定会员参加的标签")
    private String memberName;

}
