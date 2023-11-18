package com.yiling.marketing.couponactivity.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityEnterpriseGiveRequest extends QueryPageListRequest {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 企业类型
     */
    private Integer etype;

    /**
     * 终端区域编码（所属省份编码）
     */
    private String regionCode;

}
