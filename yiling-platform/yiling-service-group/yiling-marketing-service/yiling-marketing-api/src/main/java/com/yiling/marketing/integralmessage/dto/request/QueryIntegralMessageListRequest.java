package com.yiling.marketing.integralmessage.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分兑换消息配置 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralMessageListRequest extends BaseRequest {

    /**
     * 状态：1-启用 2-禁用
     */
    @Eq
    private Integer status;

    /**
     * 限制数量
     */
    private Integer limit;

}
