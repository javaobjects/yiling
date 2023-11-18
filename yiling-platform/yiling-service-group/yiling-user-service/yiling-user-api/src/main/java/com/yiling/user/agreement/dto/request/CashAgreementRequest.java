package com.yiling.user.agreement.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CashAgreementRequest extends BaseRequest {
    private static final long serialVersionUID = -5045331341880627054L;

    /**
     * 协议id
     */
    private Long agreementId;


    /**
     * 协议名称
     */
    private String agreementName;

    /**
     * 版本
     */
    private Integer version;

    /**
     *账号信息
     */
    private String easAccount;

    /**
     * 时间
     */
    private List<String> timeRange;
}
