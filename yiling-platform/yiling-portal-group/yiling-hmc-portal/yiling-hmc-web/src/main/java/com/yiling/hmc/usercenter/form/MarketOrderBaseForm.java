package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 市场订单 Form
 *
 * @author: fan.shen
 * @date: 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MarketOrderBaseForm extends BaseForm {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "id")
    private Long id ;

}
