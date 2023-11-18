package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class SaleTargetForm extends BaseForm {

    private Long id ;
    /**
     * 指标名称
     */
    @Length(max = 50,message = "最多可输入50个字")
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
    @Length(max = 50,message = "最多可输入50个字")
    private BigDecimal saleAmount;
}
