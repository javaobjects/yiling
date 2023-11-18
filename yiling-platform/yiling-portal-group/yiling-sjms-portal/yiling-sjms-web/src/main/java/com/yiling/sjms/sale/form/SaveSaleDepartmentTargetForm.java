package com.yiling.sjms.sale.form;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 销售指标部门配置
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Data
public class SaveSaleDepartmentTargetForm extends BaseForm {

    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 指标ID
     */
    @ApiModelProperty("指标ID")
    private Long saleTargetId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long departId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String departName;

    /**
     * 上一年目标单位元
     */
    @ApiModelProperty("上一年目标单位元")
    @NotNull(message = "上一年目标单位元不能为空")
    private BigDecimal lastTarget;

    /**
     * 上一年目标比例
     */
    @ApiModelProperty("上一年目标比例")
    @NotNull(message = "上一年目标比例不能为空")
    private BigDecimal lastTargetRatio;

    /**
     * 本年目标单位元
     */
    @ApiModelProperty("本年目标单位元")
    @NotNull(message = "本年目标不能为空")
    private BigDecimal currentTarget;

    /**
     * 本年一年目标比例
     */
    @ApiModelProperty("本年一年目标比例")
    @NotNull(message = "本年一年目标比例不能为空")
    private BigDecimal currentTargetRatio;
    /**
     * 本年度增加单位元
     */
    @ApiModelProperty("本年度增加单位元")
    @NotNull(message = "本年度增加不能为空")
    private BigDecimal currentIncrease;


}
