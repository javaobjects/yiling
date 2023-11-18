package com.yiling.sales.assistant.task.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 随货同行单匹配商品详情
 * </p>
 *
 * @author gxl
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AccompanyingBillMatchDetailDTO extends BaseDTO {


    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    private Long accompanyingBillId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecifications;

    /**
     * 出库日期
     */
    private Date outDate;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 基价
     */
    private BigDecimal price;

    /**
     * 销售数量
     */
    private Long quantity;



}
