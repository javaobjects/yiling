package com.yiling.b2b.app.shop.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class ShopDetailVO extends BaseVO {

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String shopName;

    /**
     * 店铺企业ID
     */
    @ApiModelProperty("企业ID")
    private Long shopEid;

    /**
     * 店铺logo
     */
    @ApiModelProperty("店铺logo")
    private String shopLogo;


    /**
     * 店铺简介
     */
    @ApiModelProperty("店铺简介")
    private String shopDesc;

    /**
     * 店铺公告
     */
    @ApiModelProperty("店铺公告")
    private String shopAnnouncement;

    /**
     * 起配金额
     */
    @ApiModelProperty("起配金额")
    private BigDecimal startAmount;

    /**
     * 商品种类
     */
    @ApiModelProperty("商品种类")
    private Integer goodsKind;

    /**
     * 月销量
     */
    @ApiModelProperty("月销量")
    private Integer monthSales;

    /**
     * 是否存在楼层
     */
    @ApiModelProperty("是否存在楼层")
    private Boolean existFloor;

    /**
     * 是否为优质商家
     */
    @ApiModelProperty("是否为优质商家")
    private Boolean highQualitySupplierFlag;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    /**
     * 采购申请状态：1-待审核 2-已建采 3-已驳回 4-未建采(此状态4为虚拟状态，仅展示给前端使用)
     */
    @ApiModelProperty("采购申请状态：1-待审核 2-已建采 3-已驳回 4-未建采（当前状态可申请建立采购关系）")
    private Integer purchaseStatus;

}
