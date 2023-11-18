package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议附件 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementAttachmentRequest extends BaseRequest {

    /**
     * 协议附件类型：1-协议原件 2-协议复印件
     */
    private Integer attachmentType;

    /**
     * 附件KEY
     */
    private String fileKey;

    /**
     * 文件类型：1-图片 2-PDF
     */
    private Integer fileType;

}
