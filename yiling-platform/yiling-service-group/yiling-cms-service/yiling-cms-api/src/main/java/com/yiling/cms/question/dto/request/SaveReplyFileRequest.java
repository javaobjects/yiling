package com.yiling.cms.question.dto.request;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveReplyFileRequest extends BaseRequest {

    /**
     * 附件key
     */
    private String replyFileKey;

    /**
     * 文件名称
     */
    private String fileName;

}
