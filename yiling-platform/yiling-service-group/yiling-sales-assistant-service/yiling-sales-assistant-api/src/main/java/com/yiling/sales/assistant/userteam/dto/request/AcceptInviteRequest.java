package com.yiling.sales.assistant.userteam.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 接受邀请 Request
 * 
 * @author lun.yu
 * @date 2022/1/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AcceptInviteRequest extends BaseRequest {

    /**
     * 邀请方式：1-短信 2-微信
     */
    @NotNull
    private Integer inviteType;

    /**
     * 邀请人ID
     */
    @NotNull
    private Long parentId;

    /**
     * 被邀请人手机号
     */
    @NotEmpty
    private String mobilePhone;

}
