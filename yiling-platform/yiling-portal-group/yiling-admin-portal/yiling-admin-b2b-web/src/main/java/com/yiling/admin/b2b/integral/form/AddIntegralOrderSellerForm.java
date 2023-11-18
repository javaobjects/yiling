package com.yiling.admin.b2b.integral.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-商家范围添加商家 Form
 *
 * @author: lun.yu
 * @date: 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIntegralOrderSellerForm extends BaseForm {

    /**
     * 发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

    /**
     * 企业id-单独添加时使用
     */
    @ApiModelProperty("企业id-单独添加时使用")
    private Long eid;

    /**
     * 企业id集合-添加当前页时使用
     */
    @ApiModelProperty("企业id集合-添加当前页时使用")
    private List<Long> eidList;
    
}
