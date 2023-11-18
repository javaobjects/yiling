package com.yiling.admin.b2b.common.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.b2b.admin.settlement.form
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RepeatOrderProcessForm extends BaseForm {

    /**
     * 退款处理方式：1，未退款通过接口退款，2，已退款标记已处理
     */
    @ApiModelProperty(value="退款处理方式：1，未退款通过接口退款，2，已退款标记已处理",required= true)
    @NotNull
    private Integer methodType;

    /**
     * 重复退款Id
     */
    @ApiModelProperty(value= "重复退款Id",required=true)
    @NotNull
    private Long repeatOrderId;
}
