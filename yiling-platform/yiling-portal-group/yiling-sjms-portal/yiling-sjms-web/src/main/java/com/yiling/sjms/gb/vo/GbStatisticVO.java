package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 团购表单统计VO
 * </p>
 *
 * @author wei.wang
 * @date 2023-02-15
 */
@Data
public class GbStatisticVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 省区名称
     */
    @ApiModelProperty(value = "省区名称")
    private String provinceName;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String goodsName;


    /**
     * 提报盒数
     */
    @ApiModelProperty(value = "提报盒数")
    private Integer quantityBox;

    /**
     * 提报实际团购金额
     */
    @ApiModelProperty(value = "提报实际团购金额")
    private BigDecimal finalAmount;

    /**
     * 取消盒数
     */
    @ApiModelProperty(value = "取消盒数")
    private Integer cancelQuantityBox;

    /**
     * 取消金额
     */
    @ApiModelProperty(value = "取消金额")
    private BigDecimal cancelFinalAmount;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    private Date dayTime;


    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private String monthDate;


}
