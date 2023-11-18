package com.yiling.admin.b2b.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单送积分-平台SKU VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralGivePlatformGoodsVO extends BaseVO {

    /**
     * 发放规则ID
     */
    @ApiModelProperty("发放规则ID")
    private Long giveRuleId;

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
    @ApiModelProperty("是否以岭品标识 以岭标识 0:非以岭 1：以岭")
    private Integer ylFlag;
}
