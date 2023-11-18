package com.yiling.export.export.bo;

import lombok.Data;

/**
 * 团购复核
 */
@Data
public class GbFormReviewMessageBO {

    /**
     * 表单ID
     */
    private Long formId;
    /**
     * 是否复核: 1-否 2-是
     */
    private Integer reviewStatus;

    /**
     * 复核意见
     */
    private String reviewReply;


}
