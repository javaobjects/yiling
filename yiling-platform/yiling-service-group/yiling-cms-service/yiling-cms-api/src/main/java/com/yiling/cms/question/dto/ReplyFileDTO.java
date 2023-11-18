package com.yiling.cms.question.dto;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReplyFileDTO extends BaseDTO {

    /**
     * 附件key
     */

    private String replyFileKey;

    /**
     * 文件名称
     */
    private String fileName;

}
