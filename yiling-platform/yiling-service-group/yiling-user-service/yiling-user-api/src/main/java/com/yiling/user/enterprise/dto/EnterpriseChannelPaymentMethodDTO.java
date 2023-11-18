package com.yiling.user.enterprise.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 渠道支付方式 dto
 *
 * @author yuecheng.chen
 * @date 2021-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseChannelPaymentMethodDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 支付方式ID
     */
    private Long paymentMethodId;


}
