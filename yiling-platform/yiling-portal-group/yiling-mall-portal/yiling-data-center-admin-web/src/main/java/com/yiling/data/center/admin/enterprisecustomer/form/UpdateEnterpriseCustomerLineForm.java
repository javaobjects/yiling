package com.yiling.data.center.admin.enterprisecustomer.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新企业客户产品线 Form
 *
 * @author: lun.yu
 * @date: 2021/11/29
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseCustomerLineForm extends BaseForm {

    /**
     * 客户ID
     */
    @NotNull
    @ApiModelProperty("客户ID")
    private Long customerEid;

    /**
     * 客户名称
     */
    @NotEmpty
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 使用产品线：1-POP 2-B2B
     */
    @NotEmpty
    @ApiModelProperty("使用产品线：1-POP 2-B2B")
    private List<Integer> useLineList;

}
