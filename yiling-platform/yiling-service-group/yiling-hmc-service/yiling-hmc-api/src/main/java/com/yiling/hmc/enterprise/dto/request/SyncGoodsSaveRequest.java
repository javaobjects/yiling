package com.yiling.hmc.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SyncGoodsSaveRequest extends BaseRequest {

    /**
     * 同步商品信息
     */
    private List<SyncGoodsSaveDetailRequest> detailList;
}
