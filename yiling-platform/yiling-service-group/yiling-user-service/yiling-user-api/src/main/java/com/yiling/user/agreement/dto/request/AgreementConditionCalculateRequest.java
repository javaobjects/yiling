package com.yiling.user.agreement.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementConditionCalculateRequest extends BaseRequest {

    private static final long serialVersionUID = -5045331341880627054L;

    private Long eid;

    /**
     * 满足条件的订单集合
     */
    private List<Long> orderIds;

    /**
     *
     */
    private List<Long> returnOrderIds;

    /**
     *
     */
    private Integer type;

    /**
     * 账号信息
     */
    private String easAccount;

    /**
     * 协议Id
     */
    private Long       agreementId;

    /**
     * 协议政策
     */
    private BigDecimal policyValue;

    /**
     * 协议条件
     */
    private Long       agreementConditionId;

    /**
     * 计算时间
     */
    private Date calculateTime;
}
