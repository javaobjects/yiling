package com.yiling.admin.cms.question.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveQuestionReplyForm extends BaseForm {

    /**
     * 问题ID
     */
    @NotNull
    @ApiModelProperty(value = "疑问ID",required = true)
    private  Long questionId;

    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private  String replyContent;

    /**
     * 关联的文献ID
     */
    @ApiModelProperty(value = "关联的文献ID")
    private List<Long> replyDocumentIdList;

    /**
     * 回复关联链接地址
     */
    @ApiModelProperty(value = "关联链接地址")
    private List<String> replyUrlList;

    /**
     * 回复文件关联
     */
    @ApiModelProperty(value = "关联文件key")
    private List<SaveReplyFileForm> replyFileKeyList;

    /**
     * 关联图片
     */
    @ApiModelProperty(value = "关联图片key")
    private List<String> replyPictureList;
}
