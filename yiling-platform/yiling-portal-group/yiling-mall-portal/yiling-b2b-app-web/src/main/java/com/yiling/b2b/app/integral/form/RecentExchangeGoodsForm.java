package com.yiling.b2b.app.integral.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 立即兑换 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-18
 */
@Data
@Accessors(chain = true)
public class RecentExchangeGoodsForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    /**
     * 兑换数量
     */
    @NotNull
    @ApiModelProperty(value = "兑换数量", required = true)
    private Integer exchangeNum;

    /**
     * 兑换地址ID（真实物品时需要）
     */
    @ApiModelProperty("兑换地址ID（真实物品时需要）")
    private Integer addressId;


}
