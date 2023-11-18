package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 提交理赔资料详情 Form
 *
 * @author: fan.shen
 * @date: 2022/7/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitClaimInformationForm extends BaseForm {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long id;

    /**
     * 订单小票
     */
    @ApiModelProperty("订单小票")
    private List<String> orderReceiptsList;

    /**
     * 身份证正面
     */
    @NotEmpty(message = "身份证正面不能为空")
    @ApiModelProperty("身份证正面")
    private String idCardFront;

    /**
     * 身份证背面
     */
    @ApiModelProperty("身份证背面")
    @NotEmpty(message = "身份证背面不能为空")
    private String idCardBack;

    /**
     * 手写签名
     */
    @ApiModelProperty("手写签名")
    @NotEmpty(message = "手写签名不能为空")
    private String handSignature;

}
