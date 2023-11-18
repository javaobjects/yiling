package com.yiling.admin.b2b.common.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundReTryForm extends BaseForm {

    @ApiModelProperty(value = "退款单id")
    private Long refundId;
    
    @ApiModelProperty(value = "1-已退款，仅标记已处理  2-未退款，通过接口退款")
    private Integer operate;
}
