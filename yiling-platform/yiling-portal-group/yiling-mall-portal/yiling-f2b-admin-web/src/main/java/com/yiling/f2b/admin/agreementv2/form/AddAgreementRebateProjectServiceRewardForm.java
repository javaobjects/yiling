package com.yiling.f2b.admin.agreementv2.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利-项目服务奖励阶梯 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateProjectServiceRewardForm extends BaseForm {

    /**
     * 覆盖率
     */
    @ApiModelProperty("覆盖率")
    private BigDecimal coverRatio;

    /**
     * 覆盖率单位：1-% 2-元 3-盒
     */
    @ApiModelProperty("覆盖率单位：1-% 2-元 3-盒")
    private Integer coverRatioUnit;

    /**
     * 其它
     */
    @ApiModelProperty("其它")
    private BigDecimal other;

    /**
     * 其它单位：1-% 2-元 3-盒
     */
    @ApiModelProperty("其它单位：1-% 2-元 3-盒")
    private Integer otherUnit;

    /**
     * 返利
     */
    @ApiModelProperty("返利")
    private BigDecimal rebateNum;

    /**
     * 返利单位：1-% 2-元 3-盒
     */
    @ApiModelProperty("返利单位：1-% 2-元 3-盒")
    private Integer rebateNumUnit;

}