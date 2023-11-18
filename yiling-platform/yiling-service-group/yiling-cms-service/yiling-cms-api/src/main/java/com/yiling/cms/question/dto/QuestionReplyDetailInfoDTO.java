package com.yiling.cms.question.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 回复详细信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionReplyDetailInfoDTO extends BaseDTO {

    /**
     * 回复ID
     */
    private Long replyId;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复关联文献ID
     */
    private List<Long> replyDocumentIdList;

    /**
     * 回复关联链接地址
     */
    private List<String> replyUrlList;

    /**
     * 回复文件关联
     */
    private List<ReplyFileDTO> replyFileKeyList;

    /**
     * 关联图片
     */
    private List<String> replyPictureKeyList;

    /**
     * 回复人ID
     */
    private Long createUser;

    /**
     * 回复时间
     */
    private Date createTime;


}
