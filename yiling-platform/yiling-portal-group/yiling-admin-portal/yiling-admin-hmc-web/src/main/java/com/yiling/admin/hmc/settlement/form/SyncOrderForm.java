package com.yiling.admin.hmc.settlement.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 同步订单到保司接口请求参数
 *
 * @author: yong.zhang
 * @date: 2022/7/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SyncOrderForm extends BaseForm {

    @ApiModelProperty("订单id")
    private Long id;
}
