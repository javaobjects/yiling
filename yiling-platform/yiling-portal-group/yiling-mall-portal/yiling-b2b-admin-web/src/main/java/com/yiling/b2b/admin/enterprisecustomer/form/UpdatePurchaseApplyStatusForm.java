package com.yiling.b2b.admin.enterprisecustomer.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 采购申请审核 Form
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@Data
public class UpdatePurchaseApplyStatusForm extends BaseForm {

   @ApiModelProperty("ID")
   @NotNull
   private Long id;

   @Range(min = 2,max = 3)
   @ApiModelProperty("审核状态：2-审核通过 3-审核驳回")
   @NotNull
   private Integer authStatus;

   @Length(max = 100)
   @ApiModelProperty("驳回原因")
   private String authRejectReason;
}
