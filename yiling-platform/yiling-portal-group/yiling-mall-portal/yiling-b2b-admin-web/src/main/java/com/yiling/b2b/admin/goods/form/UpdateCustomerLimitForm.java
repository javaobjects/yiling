package com.yiling.b2b.admin.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerLimitForm extends BaseForm {

    /**
     * 是否控价
     */
    @ApiModelProperty("是否控制价格：0否 1是")
    private Integer limitFlag;

    /**
     * 客户Id
     */
    @ApiModelProperty("客户Id")
    private List<Long> customerEids;
}
