package com.yiling.user.agreementv2.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议归档 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateArchiveAgreementRequest extends BaseRequest {

    /**
     * 协议ID
     */
    @NotNull
    @Min(1)
    private Long id;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    @NotEmpty
    @Length(max = 10)
    private String archiveNo;

    /**
     * 审核拒绝原因
     */
    @NotEmpty
    @Length(max = 200)
    private String archiveRemark;

}
