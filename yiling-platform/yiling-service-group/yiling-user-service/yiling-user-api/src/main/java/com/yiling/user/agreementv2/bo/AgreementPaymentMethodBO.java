package com.yiling.user.agreementv2.bo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议付款方式 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementPaymentMethodBO extends BaseVO implements Serializable {

    /**
     * 付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑
     */
    @ApiModelProperty("付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑（见字典：agreement_pay_method）")
    private Integer payMethod;

    /**
     * 占比
     */
    @ApiModelProperty("占比（整数1-100）")
    private Integer ratio;

}
