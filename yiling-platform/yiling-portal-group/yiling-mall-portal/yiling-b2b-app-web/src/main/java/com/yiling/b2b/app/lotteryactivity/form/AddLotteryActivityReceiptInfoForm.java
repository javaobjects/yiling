package com.yiling.b2b.app.lotteryactivity.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加抽奖活动收货信息 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddLotteryActivityReceiptInfoForm extends BaseForm {

    /**
     * 参与抽奖明细ID
     */
    @NotNull
    @ApiModelProperty(value = "参与抽奖明细ID（我的奖品列表的ID）", required = true)
    private Long joinDetailId;

    /**
     * 收货地址ID
     */
    @NotNull
    @ApiModelProperty(value = "收货地址ID", required = true)
    private Long deliveryAddressId;

}
