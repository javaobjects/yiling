package com.yiling.cms.question.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @describe 保存类型为1(问题库区分:1-问题知识库 2-医药代表社区库)的问题
 * @author:wei.wang
 * @date:2022/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveQuestionRequest extends BaseRequest {

	private static final long serialVersionUID = -4484861173272278875L;

	/**
     * id
	 */
	private Long id;
    /**
     * 问题标题
     */
    private String title;

    /**
     * 内容详情
     */
    private String content;

    /**
     * 问题库区分 1-问题知识库 2-医药代表社区库
     */
    private Integer type;

    /**
     * 图片视频key
     */
    private List<String> keyList;

    /**
     * 所属分类：1-药品相关
     */
    private Integer categoryId;

    /**
     * 关联文献id集合
     */
    private List<Long> documentIdList;

    /**
     * 关联标准库商品信息
     */
    private List<QuestionStandardGoodsInfoRequest> standardInfoList;

    /**
     * 提问人名称
     */
    private String fromUserName;

    /**
     * 提问人Id
     */
    private Long fromUserId;


    /**
     * 提问人所属医院
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
     * 最新回复时间
     */
    private Date lastReplyTime;

    /**
     * 描述
     */
    private String description;
}
