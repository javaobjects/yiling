package com.yiling.f2b.admin.agreementv2.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议附件 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementAttachmentForm extends BaseForm {

    /**
     * 协议附件类型：1-协议原件 2-协议复印件
     */
    @ApiModelProperty("协议附件类型：1-协议原件 2-协议复印件")
    private Integer attachmentType;

    /**
     * 附件KEY
     */
    @ApiModelProperty("附件KEY")
    private String fileKey;

    /**
     * 文件类型：1-图片 2-PDF
     */
    @ApiModelProperty("文件类型：1-图片 2-PDF")
    private Integer fileType;

}
