package com.yiling.user.member.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员退款更新企业会员 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateReturnEnterpriseMemberRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 会员ID
     */
    @NotNull
    private Long memberId;

    /**
     * 会员退款订单号
     */
    @NotEmpty
    private String orderNo;

}
