package com.yiling.hmc.common.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询有效广告请求参数
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdvertisementListRequest extends BaseRequest {

    /**
     * 投放位置:1-C端用户侧首页 2-C端用户侧我的
     */
    private Integer position;

    /**
     * 查询数量
     */
    private Integer count;
}
