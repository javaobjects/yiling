package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改企业渠道 Request
 *
 * @author: xuan.zhou
 * @date: 2022/3/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseChannelRequest extends BaseRequest {

    @NotNull
    private Long eid;

    @NotNull
    private EnterpriseChannelEnum enterpriseChannelEnum;
}
