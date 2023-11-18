package com.yiling.hmc.common.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdvertisementPageRequest extends QueryPageListRequest {

    /**
     * 投放位置: 0-所有 1-C端用户侧首页 2-C端用户侧我的(多选，逗号隔开)
     */
    private Integer position;
}
