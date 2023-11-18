package com.yiling.settlement.report.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class ReportPurchaseStockOccupyDTO extends BaseDTO {

    /**
     * 报表id
     */
    private Long reportId;

    /**
     * b2b报表明细id
     */
    private Long b2bDetailId;

    /**
     * 流向报表明细id
     */
    private Long flowDetailId;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    private Integer type;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * erp订单号
     */
    private String soNo;

    /**
     * 商业id
     */
    private Long sellerEid;

    /**
     * 商业名称
     */
    private String sellerName;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * b2b商品内码
     */
    private String goodsErpCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * erp商品规格
     */
    private String soSpecifications;

    /**
     * b2b订单以岭商品id
     */
    private Long goodsId;

    /**
     * 以岭商品id
     */
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecification;

    /**
     * 流向报表销售数量
     */
    private Integer soQuantity;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;

    /**
     * 单位
     */
    private String soUnit;

    /**
     *  购进渠道：1-大运河采购 2-京东采购 3-库存不足
     */
    private Integer purchaseChannel;
}
