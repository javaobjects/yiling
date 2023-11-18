package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议控销区域 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementControlAreaRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 供销商品组ID
     */
    private Long controlGoodsGroupId;

    /**
     * 控销区域描述
     */
    private String description;

    /**
     * 控销区域Json串
     */
    private String jsonContent;

}
