package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * <p>
 * 团购表单统计
 * </p>
 *
 * @author wei.wang
 * @date 2023-02-15
 */
@Data
public class GbStatisticBO  {

    private static final long serialVersionUID = 1L;

    /**
     * 省区名称
     */
    private String provinceName;

    /**
     * 产品名称
     */
    private String goodsName;


    /**
     * 提报盒数
     */
    private Integer quantityBox;

    /**
     * 提报实际团购金额
     */
    private BigDecimal finalAmount;

    /**
     * 取消盒数
     */
    private Integer cancelQuantityBox;

    /**
     * 取消金额
     */
    private BigDecimal cancelFinalAmount;

    /**
     * 日期
     */
    private String dayDate;

    /**
     * 团购月份
     */
    private String monthDate;


}
