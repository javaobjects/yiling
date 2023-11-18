package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Data
public class ExportFlowGoodsBatchPageListBO {

    /**
     * 商品名称
     */
    private String gbName;

    /**
     * 商品规格
     */
    private String gbSpecifications;

    /**
     * 批准文号
     */
    private String gbLicense;

    /**
     * 商品生产厂家
     */
    private String gbManufacturer;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 批次号
     */
    private String gbBatchNo;

    /**
     * 生产日期
     */
    private String gbProduceTime;

    /**
     * 有效日期
     */
    private String gbEndTime;

    /**
     * 入库日期
     */
    private Date gbTime;

    /**
     * 库存数量
     */
    private BigDecimal gbNumber;

    /**
     * 商品单位
     */
    private String gbUnit;

    /**
     * 总计数量（按规格维度数量求和）
     */
    private BigDecimal totalNumber;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 商品内码
     */
    private String inSn;

    /**
     * 库存日期
     */
    private Date exportTime;

    /**
     * 订单来源，1-大运河库存 2-非大运河库存
     */
    private String gbSource;

    /**
     * 商品规格标准ID
     */
    private Long specificationId;

    /**
     * 标准商品名称
     */
    private String standardName;

    /**
     * 标准商品规格
     */
    private String standardSellSpecifications;

}
