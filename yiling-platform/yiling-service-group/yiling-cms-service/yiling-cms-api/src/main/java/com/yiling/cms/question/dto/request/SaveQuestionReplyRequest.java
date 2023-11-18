package com.yiling.cms.question.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveQuestionReplyRequest extends QueryPageListRequest {

    /**
     * 问题ID
     */
    private  Long questionId;

    /**
     * 回复内容
     */
    private  String replyContent;

    /**
     * 关联的文献ID
     */
    private List<Long> replyDocumentIdList;

    /**
     * 回复关联链接地址
     */
    private List<String> replyUrlList;

    /**
     * 回复文件关联
     */
    private List<SaveReplyFileRequest> replyFileKeyList;

    /**
     * 关联图片
     */
    private List<String> replyPictureList;
}
