package com.yiling.admin.hmc.goods.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 HmcGoodsBO
 * @描述
 * @创建时间 2022/3/31
 * @修改人 shichen
 * @修改时间 2022/3/31
 **/
@Data
public class HmcGoodsVO extends BaseVO {
    private Long id;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long eid;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String ename;

    /**
     * 保险药品id
     */
    @ApiModelProperty(value = "保险药品id")
    private Long goodsId;

    /**
     * 保险药品名称
     */
    @ApiModelProperty(value = "保险药品名称")
    private String goodsName;

    /**
     * 注册证号（批准文号）
     */
    @ApiModelProperty(value = "注册证号（批准文号）")
    private String licenseNo;



    /**
     * 标准库ID
     */
    @ApiModelProperty(value = "标准库ID")
    private Long standardId;

    /**
     * 标准库规格ID
     */
    @ApiModelProperty(value = "标准库规格ID")
    private Long sellSpecificationsId;

    /**
     * 标准库销售规格
     */
    @ApiModelProperty(value = "标准库销售规格")
    private String sellSpecifications;

    /**
     * 销售规格
     */
    @ApiModelProperty(value = "销售规格")
    private String specifications;
    /**
     * 标准库销售单位
     */
    @ApiModelProperty(value = "标准库销售单位")
    private String sellUnit;
    /**
     * 规格单位
     */
    @ApiModelProperty(value = "规格单位")
    private String unit;

    /**
     * 商家售卖金额/盒
     */
    @ApiModelProperty(value = "商家售卖金额/盒")
    private BigDecimal sellerPrice;

    /**
     * 给终端结算额/盒
     */
    @ApiModelProperty(value = "给终端结算额/盒")
    private BigDecimal settlePrice;

    /**
     * 商品状态 1上架，2下架
     */
    @ApiModelProperty(value = "商品状态 1上架，2下架")
    private Integer goodsStatus;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Long qty;

    /**
     * 库存冻结数量
     */
    @ApiModelProperty(value = "药+险所需库存数量")
    private Long frozenQty;

    @ApiModelProperty(value = "skuId 修改库存使用")
    private Long skuId;

    /**
     * 标准库分类
     */
    @ApiModelProperty(value = "标准库分类")
    private String standardCategoryName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * IH C端平台药房id
     */
    @ApiModelProperty(value = "C端平台药房id")
    private Long ihCPlatformId;

    /**
     * IH 配送id
     */
    private Long ihEid;

    /**
     * IH 配送商商品ID
     */
    @ApiModelProperty(value = "配送商商品ID")
    private Long ihPharmacyGoodsId;

    @ApiModelProperty(value = "IH 商品信息")
    private IHGoodsInfoVO ihGoodsInfoVO;
}
