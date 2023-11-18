package com.yiling.user.agreementv2.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加非商品返利 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementOtherRebateRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 非商品返利方式：1-会务费 2-流向返利 3-破损返利 4-维价返利 5-控销返利 6-如期回款返利 7-其他返利
     */
    private Integer rebateType;

    /**
     * 金额类型：1-销售金额 2-购进金额 3-付款金额 4-固定金额
     */
    private Integer amountType;

    /**
     * 百分比或固定金额值
     */
    private BigDecimal amount;

    /**
     * 单位：1-固定金额 2-百分比
     */
    private Integer unit;

    /**
     * 是否含税：0-否 1-是
     */
    private Boolean taxFlag;

}
