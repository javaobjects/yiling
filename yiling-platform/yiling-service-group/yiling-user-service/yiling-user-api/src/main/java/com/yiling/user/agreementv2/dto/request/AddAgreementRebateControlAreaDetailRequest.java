package com.yiling.user.agreementv2.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利范围控销区域详情 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateControlAreaDetailRequest extends BaseRequest {

    /**
     * 协议返利范围ID
     */
    private Long rebateScopeId;

    /**
     * 区域编码
     */
    private String areaCode;

}
