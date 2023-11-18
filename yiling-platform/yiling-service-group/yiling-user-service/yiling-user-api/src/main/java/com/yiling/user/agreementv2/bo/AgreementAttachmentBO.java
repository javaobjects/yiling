package com.yiling.user.agreementv2.bo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议附件 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementAttachmentBO extends BaseVO implements Serializable {

    /**
     * 协议附件类型：1-协议原件 2-协议复印件
     */
    private Integer attachmentType;

    /**
     * 附件KEY
     */
    private String fileKey;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 文件类型：1-图片 2-PDF
     */
    private Integer fileType;

}
