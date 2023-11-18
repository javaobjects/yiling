package com.yiling.user.agreementv2.dto.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议审核 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateAuthAgreementRequest extends BaseRequest {

    /**
     * 协议ID
     */
    @NotNull
    private Long id;

    /**
     * 纸质件编号
     */
    private String paperNo;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    @NotNull
    private Integer authStatus;

    /**
     * 审核拒绝原因
     */
    private String authRejectReason;

}
