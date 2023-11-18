package com.yiling.admin.cms.question.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveReplyFileForm extends BaseForm {

    /**
     * 附件key
     */
    @ApiModelProperty(value = "附件key")
    private String replyFileKey;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

}
