package com.yiling.hmc.settlement.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SettlementEnterprisePageRequest extends BaseRequest {

    /**
     * 服务终端id集合
     */
    private List<Long> eidList;
}
