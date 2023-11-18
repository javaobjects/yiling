package com.yiling.marketing.paypromotion.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPaypromotionSellerLimitPageRequest extends QueryPageListRequest {

    /**
     * 营销活动id
     */
    private Long marketingPayId;

    /**
     * 企业ID-精确搜索
     */
    private Long eid;

    /**
     * 企业名称-模糊搜索
     */
    private String ename;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;
}
