package com.yiling.user.enterprise.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 给企业开通平台 Request
 *
 * @author: xuan.zhou
 * @date: 2021/10/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenPlatformRequest extends BaseRequest {

    private static final long serialVersionUID = -8820123607712534932L;

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 开通的平台类型枚举列表
     */
    @NotEmpty
    private List<PlatformEnum> platformEnumList;

    /**
     * 渠道类型枚举，开通POP平台时不能为空
     */
    private EnterpriseChannelEnum enterpriseChannelEnum;

    /**
     * HMC类型枚举，开通HMC时不能为空
     */
    private EnterpriseHmcTypeEnum enterpriseHmcTypeEnum;
}
