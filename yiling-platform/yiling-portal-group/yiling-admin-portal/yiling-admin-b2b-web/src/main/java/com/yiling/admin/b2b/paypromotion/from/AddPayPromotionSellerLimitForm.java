package com.yiling.admin.b2b.paypromotion.from;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddPayPromotionSellerLimitForm extends BaseForm {

    @ApiModelProperty("支付促销id")
    @NotNull(message = "支付促销id")
    private Long marketingStrategyId;

    @ApiModelProperty("企业id集合-添加当前页时使用")
    private List<Long> eidList;
}
