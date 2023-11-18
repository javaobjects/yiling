package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.settlement.report.enums.ReportTypeEnum;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockOccupyPageItemVO extends BaseVO {

    /**
     * 报表id
     */
    @ApiModelProperty("报表id")
    private Long reportId;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    @ApiModelProperty("报表类型：1-B2B返利 2-流向返利")
    private Integer type;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * erp订单号
     */
    @ApiModelProperty("erp订单号")
    private String soNo;

    /**
     * 商业id
     */
    @ApiModelProperty("商业id")
    private Long sellerEid;

    /**
     * 商业名称
     */
    @ApiModelProperty("商业名称")
    private String sellerName;

    /**
     * 商品内码
     */
    @ApiModelProperty("商品内码")
    private String goodsInSn;

    /**
     * b2b商品内码
     */
    @JsonIgnore
    @ApiModelProperty(value = "b2b商品内码",hidden = true)
    private String goodsErpCode;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty("商品规格")
    private String specifications;

    /**
     * erp商品规格
     */
    @JsonIgnore
    @ApiModelProperty(value = "erp商品规格",hidden = true)
    private String soSpecifications;

    /**
     * b2b订单以岭商品id
     */
    @JsonIgnore
    @ApiModelProperty(value = "b2b订单以岭商品id",hidden = true)
    private Long goodsId;

    /**
     * 以岭商品id
     */
    @ApiModelProperty("以岭商品id")
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    @ApiModelProperty("以岭商品名称")
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    @ApiModelProperty("以岭商品规格")
    private String ylGoodsSpecification;

    /**
     * 流向报表销售数量
     */
    @ApiModelProperty("数量")
    private Integer soQuantity;

    /**
     * 收货数量
     */
    @JsonIgnore
    @ApiModelProperty(value = "收货数量",hidden = true)
    private Integer receiveQuantity;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String soUnit;

    /**
     *  购进渠道：1-大运河采购 2-京东采购 3-库存不足
     */
    @ApiModelProperty("购进渠道：1-大运河采购 2-京东采购 3-库存不足")
    private Integer purchaseChannel;


    public String getGoodsInSn() {
        if (ObjectUtil.equal(type, ReportTypeEnum.B2B.getCode())){
            return goodsErpCode;
        }else {
            return goodsInSn;
        }
    }

    public String getSpecifications() {
        if (ObjectUtil.equal(type, ReportTypeEnum.B2B.getCode())){
            return specifications;
        }else {
            return soSpecifications;
        }
    }

    public Long getYlGoodsId() {
        if (ObjectUtil.equal(type, ReportTypeEnum.B2B.getCode())){
            return goodsId;
        }else {
            return ylGoodsId;
        }
    }

    public String getYlGoodsName() {
        if (ObjectUtil.equal(type, ReportTypeEnum.B2B.getCode())){
            return goodsName;
        }else {
            return ylGoodsName;
        }
    }

    public String getYlGoodsSpecification() {
        if (ObjectUtil.equal(type, ReportTypeEnum.B2B.getCode())){
            return specifications;
        }else {
            return ylGoodsSpecification;
        }
    }

    public Integer getSoQuantity() {
        if (ObjectUtil.equal(type, ReportTypeEnum.B2B.getCode())){
            return receiveQuantity;
        }else {
            return soQuantity;
        }
    }
}
