package com.yiling.cms.question.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryQuestionPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 1515883922320291419L;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建开始时间
     */
    private Date startCreateTime;

    /**
     * 创建结束时间
     */
    private Date endCreateTime;

    /**
     * 是否回复 1-未回复 2-已回复 3-不需要回复
     */
    private Integer replyFlag;

    /**
     * 问题库区分 1-问题知识库 2-医药代表社区库
     */
    private Integer type;

    /**
     * 标准库id
     */
    private Long standardId;

    /**
     * 标准库商品规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 被提问的医药代表
     */
    private Long toUserId;

    /**
     * 提问人
     */
    private Long fromUserId;



}
