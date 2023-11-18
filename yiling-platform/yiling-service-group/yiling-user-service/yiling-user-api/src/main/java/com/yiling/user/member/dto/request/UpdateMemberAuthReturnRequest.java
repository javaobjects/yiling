package com.yiling.user.member.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员同意/驳回退款 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMemberAuthReturnRequest extends BaseRequest {

    /**
     * 退款ID
     */
    @NotNull
    private Long id;

    /**
     * 审核状态：2-已审核 3-已驳回
     */
    @NotNull
    private Integer authStatus;

}
