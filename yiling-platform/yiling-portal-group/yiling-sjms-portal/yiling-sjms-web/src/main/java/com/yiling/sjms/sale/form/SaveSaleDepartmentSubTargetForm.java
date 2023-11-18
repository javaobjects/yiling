package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 部门销售指标子项配置
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSaleDepartmentSubTargetForm extends BaseForm {

    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 指标ID
      */
    @ApiModelProperty("指标ID")
    @NotNull
    private Long saleTargetId;
    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @NotNull
    private Long departId;
    /**
     * 指标配置类型1-月度2-省区3-品种4区办
     */
    @ApiModelProperty("指标配置类型1-月度2-省区3-品种4区办")
    @NotNull
    private Integer type;

    /**
     * 类型关联ID:部门ID,商品品类ID
     */
    @ApiModelProperty("类型关联ID:部门ID,商品品类ID")
    @NotNull
    private Long relId;

    /**
     * 类型关联名称：部门名称,商品品类名称
     */
    @ApiModelProperty("类型关联名称：部门名称,商品品类名称")
    @NotBlank
    private String relName;

    /**
     * 上一年目标单位元
     */
    @ApiModelProperty("本年目标单位元")
    @NotNull(message = "本年目标不能为空")
    private BigDecimal lastTarget;

    /**
     * 上一年目标比例
     */
    @ApiModelProperty("上一年目标比例")
    @NotNull(message = "本年目标比例不能为空")
    private BigDecimal lastTargetRatio;

    /**
     * 本年目标单位元
     */
    @ApiModelProperty("本年目标单位元")
    @NotNull(message = "本年目标单位不能为空")
    private BigDecimal currentTarget;

    /**
     * 本年一年目标比例
     */
    @ApiModelProperty("本年一年目标比例")
    @NotNull(message = "本年一年目标不能为空")
    private BigDecimal currentTargetRatio;

    /**
     * 本年度增加单位元
     */
    @ApiModelProperty("本年度增加单位元")
    @NotNull(message = "本年度增加不能为空")
    private BigDecimal currentIncrease;


}
