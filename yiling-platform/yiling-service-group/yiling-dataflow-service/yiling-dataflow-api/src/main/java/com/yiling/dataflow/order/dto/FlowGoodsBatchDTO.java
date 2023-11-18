package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * erp库存流向
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowGoodsBatchDTO extends BaseDTO {

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 商品名称
     */
    private String gbName;

    /**
     * 药品内码（供应商的ERP的商品主键）
     */
    private String inSn;

    /**
     * 入库时间
     */
    private Date gbTime;

    /**
     * 入库时间
     */
    private String gbMonth;

    /**
     * 商品规格
     */
    private String gbSpecifications;

    /**
     * 商品规格id
     */
    private Long specificationId;

    /**
     * 商品批准文号
     */
    private String gbLicense;

    /**
     * 商品生产厂家
     */
    private String gbManufacturer;

    /**
     * 商品批次号
     */
    private String gbBatchNo;

    /**
     * 生产时间
     */
    private String gbProduceTime;

    /**
     * 有效期
     */
    private String gbEndTime;

    /**
     * 库存数量
     */
    private BigDecimal gbNumber;

    private BigDecimal totalNumber;

    /**
     * 商品单位
     */
    private String gbUnit;

    /**
     * 订单来源，1-大运河库存 2-非大运河库存
     */
    private String gbSource;

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

    /**
     * 统计库存总数量状态：0-未统计，1-已统计
     */
    private Integer statisticsStatus;

}
