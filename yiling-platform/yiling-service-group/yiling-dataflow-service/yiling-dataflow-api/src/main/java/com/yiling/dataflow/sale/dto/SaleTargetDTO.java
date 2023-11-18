package com.yiling.dataflow.sale.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleTargetDTO extends BaseDTO {
    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标编号
     */
    private String targetNo;

    /**
     * 指标年份
     */
    private Long targetYear;

    /**
     * 销售目标金额
     */
    private BigDecimal saleAmount;

    private Date updateTime;

}
