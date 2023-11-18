package com.yiling.b2b.app.order.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** B2B订单确认form
 * @author zhigang.guo
 * @date: 2022/12/7
 */
@Data
public class B2BOrderVerificationForm extends BaseForm {

    @ApiModelProperty(value = "配送商企业信息", required = true)
    @NotEmpty
    private List<@Valid VerificationDistributorForm> distributorList;

    @Data
    public static class  VerificationDistributorForm {

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "卖家企业ID", required = true)
        private Long distributorEid;

        @ApiModelProperty(value = "是否有赠品", required = true)
        @NotNull
        private Boolean hasGift;
    }

}
