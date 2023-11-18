package com.yiling.admin.b2b.strategy.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 策略满赠平台SKU
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyPlatformGoodsLimitVO extends BaseVO {

    /**
     * 营销活动id
     */
    @ApiModelProperty("营销活动id")
    private Long marketingStrategyId;

    /**
     * 标准库ID
     */
    @ApiModelProperty("标准库ID")
    private Long standardId;

    /**
     * 规格ID
     */
    @ApiModelProperty("规格ID")
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    //===========================================
    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品 7 医疗器械
     */
    @ApiModelProperty("商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品 7 医疗器械")
    private Integer goodsType;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;

    /**
     * 生产厂家
     */
    @ApiModelProperty("生产厂家")
    private String manufacturer;

    /**
     * 是否以岭品标识 以岭标识 0:非以岭  1：以岭
     */
    private Integer ylFlag;

    /**
     * 支付促销活动id
     */
    private Long marketingPayId;
}
