package com.yiling.admin.b2b.integral.vo;

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
 * @date 2023-01-09
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
     * 近30天兑换数量
     */
    @ApiModelProperty("近30天兑换数量")
    private Integer recentExchangeNum;

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

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建人手机号
     */
    @ApiModelProperty("创建人手机号")
    private String mobile;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

}
