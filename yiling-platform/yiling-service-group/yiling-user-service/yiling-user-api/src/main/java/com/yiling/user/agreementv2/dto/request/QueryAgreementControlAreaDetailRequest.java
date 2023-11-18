package com.yiling.user.agreementv2.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询协议控销区域详情 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementControlAreaDetailRequest extends BaseRequest {

    /**
     * 协议ID
     */
    @NotNull
    private Long agreementId;

    /**
     * 供销商品组ID
     */
    private Long controlGoodsGroupId;

}
