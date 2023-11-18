package com.yiling.b2b.app.lotteryactivity.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 删除抽奖活动-奖品收货地址 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteDeliveryAddressForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

}
