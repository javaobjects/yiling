package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/2/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowPurchaseDTO extends BaseDTO {
    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * op库主键
     */
    private String poId;

    /**
     * Erp采购订单号
     */
    private String poNo;

    /**
     * 供应商编码（供应商内码）
     */
    private String enterpriseInnerCode;

    /**
     * 供应商名称
     */
    private String enterpriseName;

    /**
     * 供应商Id
     */
    private Long supplierId;

    /**
     * 购进时间
     */
    private Date poTime;

    /**
     * 下单日期
     */
    private Date orderTime;

    /**
     * 购进时间
     */
    private String poMonth;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String poSpecifications;

    /**
     * 商品规格id
     */
    private Long specificationId;

    /**
     * 批次号
     */
    private String poBatchNo;

    /**
     * 生产时间
     */
    private Date poProductTime;

    /**
     * 有效期时间
     */
    private Date poEffectiveTime;

    /**
     * 采购数量
     */
    private BigDecimal poQuantity;

    /**
     * 商品单位
     */
    private String poUnit;

    /**
     * 采购价格
     */
    private BigDecimal poPrice;

    /**
     * 金额
     */
    private BigDecimal poTotalAmount;

    /**
     * 商品生产厂家
     */
    private String poManufacturer;

    /**
     * 商品批准文号
     */
    private String poLicense;

    /**
     * 订单来源，1-大运河采购 2-非大运河采购
     */
    private String poSource;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    private Long crmGoodsCode;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;

    /**
     * 以岭商品ID（冗余字段）
     */
    private Long ylGoodsId;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;
}
