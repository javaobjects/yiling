package com.yiling.admin.b2b.integral.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存订单送积分倍数配置 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralMultipleConfigForm extends BaseForm {

    /**
     * 积分发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "积分发放规则ID", required = true)
    private Long giveRuleId;

    @NotEmpty
    @ApiModelProperty(value = "积分倍数配置集合", required = true)
    public List<@Valid SaveMultipleConfigForm> multipleConfigList;

    @Data
    public static class SaveMultipleConfigForm {

        /**
         * 客户范围：1-全部客户 2-指定客户 3-指定客户范围
         */
        @NotNull
        @ApiModelProperty(value = "客户范围：1-全部客户 2-指定客户 3-指定客户范围", required = true)
        private Integer customerScope;

        /**
         * 指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员（部分会员具体到会员方案上）
         */
        @ApiModelProperty("指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员（部分会员具体到会员方案上）")
        private Integer userType;

        /***
         * 部分会员时的会员ID
         */
        @ApiModelProperty("部分会员时的会员ID")
        private Long memberId;

        /**
         * 支付方式：0-全部支付方式 1-线上支付 2-线下支付 3-账期支付
         */
        @NotNull
        @ApiModelProperty(value = "支付方式：0-全部支付方式 1-线上支付 2-线下支付 3-账期支付", required = true)
        private Integer paymentMethod;

        /**
         * 积分倍数
         */
        @NotNull
        @ApiModelProperty(value = "积分倍数", required = true)
        private BigDecimal integralMultiple;
    }

}
