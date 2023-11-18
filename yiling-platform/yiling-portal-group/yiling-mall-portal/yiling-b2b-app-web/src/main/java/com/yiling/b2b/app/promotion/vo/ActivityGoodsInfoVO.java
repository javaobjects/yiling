package com.yiling.b2b.app.promotion.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.b2b.app.goods.vo.GoodsSkuVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shixing.sun
 * @date 2022-05-06
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class ActivityGoodsInfoVO {

    @ApiModelProperty(value = "采购商id")
    private Long eid;

    @ApiModelProperty(value = "供应商id")
    private Long shopId;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "产品名称")
    private String goodsName;

    @ApiModelProperty(value = "产品id")
    private Long goodsId;

    @ApiModelProperty("活动价格")
    private BigDecimal promotionPrice;

    @ApiModelProperty("划线价")
    private BigDecimal sellingPrice;

    @ApiModelProperty("商品规格")
    private String specifications;

    @ApiModelProperty("活动是否开始")
    private Boolean isStarted;

    @ApiModelProperty("供应商和采购商是否建采")
    private Boolean isContract;

    @ApiModelProperty("图片")
    private String pic;

    @ApiModelProperty("供应商名称")
    private String ename;

    @ApiModelProperty("组合包信息")
    private CombinationPackageVO combinationPackageVO;

    @ApiModelProperty(value = "结束时间")
    private String packageName;

    @ApiModelProperty(value = "组合包商品简称")
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

    @ApiModelProperty("产品sku信息")
    private List<GoodsSkuVO> goodsSkuList;

    @ApiModelProperty("营销活动id")
    private Long promotionActivityId;

    @ApiModelProperty("最大购买数量")
    private Integer maxBuyCount;

    @ApiModelProperty("最小购买数量")
    private Integer initialNum;
}
