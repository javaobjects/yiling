package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSaleTargetForm extends BaseRequest {

    private Long id;
    /**
     * 指标名称
     */
    @ApiModelProperty("指标名称")
    @Length(max = 50, message = "最多可输入50个字")
    @NotBlank
    private String name;

    /**
     * 指标编号
     */
    @ApiModelProperty("指标编号")
    private String targetNo;

    /**
     * 指标年份
     */
    @ApiModelProperty("指标编号")
    private Long targetYear;

    /**
     * 销售目标金额
     */
    @ApiModelProperty("销售目标金额")
    private BigDecimal saleAmount;
    @ApiModelProperty("部门销售目标集合")
    @Valid
    @NotEmpty(message = "部门集合信息不能为空")
    private List<SaveSaleDepartmentTargetForm> departmentTargets;
}
