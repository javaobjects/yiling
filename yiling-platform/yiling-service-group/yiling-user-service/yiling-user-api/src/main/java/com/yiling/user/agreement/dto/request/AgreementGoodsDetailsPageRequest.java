package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementGoodsDetailsPageRequest extends QueryPageListRequest {

    /**
     * 协议id
     */
    private Long agreementId;
}
