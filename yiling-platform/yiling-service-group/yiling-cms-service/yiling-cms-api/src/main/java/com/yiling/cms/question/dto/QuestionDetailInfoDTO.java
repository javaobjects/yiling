package com.yiling.cms.question.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 疑问详细信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionDetailInfoDTO extends BaseDTO {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 所属分类：1-药品相关
     */
    private Integer categoryId;

    /**
     * 阅读量
     */
    private Long viewCount;

    /**
     * 内容详情
     */
    private String content;

    /**
     * 文献id
     */
    private List<Long> documentIdList;

    /**
     * 商品信息集合
     */
    private List<QuestionStandardGoodsInfoDTO> standardGoodsList;

    /**
     * 链接
     */
    private List<String> urlList;

    /**
     * 图片key
     */
    private List<String> keyList;

    /**
     * 提问人名称
     */
    private String fromUserName;

    /**
     * 提问人id
     */
    private Long fromUserId;

    /**
     *提问人医院名称
     */
    private String fromUserHospitalName;

    /**
     * 被提问医药代表名称
     */
    private String toUserName;

    /**
     * 被提问医药代表名称id
     */
    private Long toUserId;

    /**
     * 是否回复 1-未回复 2-已回复 3-不需要回复
     */
    private Integer replyFlag;
    /**
     * 回复信息
     */
    private List<QuestionReplyDetailInfoDTO> replyDetailList;

    /**
     * 创建时间
     */
    private Date createTime;
}

