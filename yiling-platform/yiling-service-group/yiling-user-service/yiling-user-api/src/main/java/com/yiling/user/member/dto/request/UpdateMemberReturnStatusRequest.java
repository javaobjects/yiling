package com.yiling.user.member.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-更新会员退款状态 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMemberReturnStatusRequest extends BaseRequest {

    /**
     * 退款单据ID
     */
    @NotNull
    private Long memberReturnId;

    /**
     * 此接口仅可设置状态为 3,4
     * 退款状态：1-待退款 2-退款中 3-退款成功 4-退款失败
     */
    @NotNull
    private Integer returnStatus;

}
