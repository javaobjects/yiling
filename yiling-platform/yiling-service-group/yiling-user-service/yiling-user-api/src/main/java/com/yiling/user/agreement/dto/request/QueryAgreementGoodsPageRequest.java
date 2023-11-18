package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementGoodsPageRequest extends QueryPageListRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 批准文号
     */
    private String licenseNo;
}
