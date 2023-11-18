package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询理赔资料详情 Form
 *
 * @author: fan.shen
 * @date: 2022/7/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryClaimInformationForm extends BaseForm {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long id;

}
