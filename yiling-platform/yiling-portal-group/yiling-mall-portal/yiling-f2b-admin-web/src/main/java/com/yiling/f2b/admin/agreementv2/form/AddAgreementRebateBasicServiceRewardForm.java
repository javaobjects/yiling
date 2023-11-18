package com.yiling.f2b.admin.agreementv2.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利-基础服务奖励阶梯 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateBasicServiceRewardForm extends BaseForm {

    /**
     * 增长率
     */
    @ApiModelProperty("增长率")
    private BigDecimal increaseRatio;

    /**
     * 增长率单位：1-% 2-元 3-盒
     */
    @ApiModelProperty("增长率单位：1-% 2-元 3-盒")
    private Integer increaseRatioUnit;

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
