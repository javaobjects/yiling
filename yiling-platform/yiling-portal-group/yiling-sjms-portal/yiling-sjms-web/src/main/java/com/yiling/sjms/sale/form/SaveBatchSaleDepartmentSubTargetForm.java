package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

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
public class SaveBatchSaleDepartmentSubTargetForm extends BaseForm {
   @Valid
   @NotNull
   @ApiModelProperty("省区List")
   private List<SaveSaleDepartmentSubTargetForm> provinceList;
   @Valid
   @NotNull
   @ApiModelProperty("月度List")
   private List<SaveSaleDepartmentSubTargetForm> monthList;
   @Valid
   @NotNull
   @ApiModelProperty("品种List")
   private List<SaveSaleDepartmentSubTargetForm> goodsList;
   @ApiModelProperty("区办List")
   private List<SaveSaleDepartmentSubTargetForm> areaList;
   @ApiModelProperty("指标ID")
   @NotNull
   private Long saleTargetId;
   /**
    * 部门ID
    */
   @ApiModelProperty("部门ID")
   @NotNull
   private Long departId;

   @NotNull(message = "销售目标金额不能为空 ")
   @ApiModelProperty("销售目标金额")
   private BigDecimal saleTarget;
}
