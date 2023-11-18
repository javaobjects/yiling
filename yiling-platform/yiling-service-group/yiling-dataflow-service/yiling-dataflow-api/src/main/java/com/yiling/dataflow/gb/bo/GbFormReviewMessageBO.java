package com.yiling.dataflow.gb.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/6/2
 */
@Data
public class GbFormReviewMessageBO implements Serializable {

    private static final long serialVersionUID = -5359674276810737012L;

    /**
     * 表单ID
     */
    private Long formId;

    /**
     * 是否复核: 1-否 2-是
     *
     * GbFormReviewStatusEnum
     */
    private Integer reviewStatus;

    /**
     * 复核意见
     */
    private String reviewReply;

}
