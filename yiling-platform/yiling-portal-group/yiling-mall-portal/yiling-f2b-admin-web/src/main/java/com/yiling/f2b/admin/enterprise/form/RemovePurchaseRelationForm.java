package com.yiling.f2b.admin.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 移除渠道商的采购关系 Form
 *
 * @author: yuecheng.chen
 * @date: 2021/6/8 0008
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemovePurchaseRelationForm extends BaseForm {
    @NotNull
    @ApiModelProperty(value = "采购商ID", required = true)
    private Long buyerId;

    @NotEmpty
    @ApiModelProperty(value = "供应商ID集合", required = true)
    private List<Long> sellerIds;
}
