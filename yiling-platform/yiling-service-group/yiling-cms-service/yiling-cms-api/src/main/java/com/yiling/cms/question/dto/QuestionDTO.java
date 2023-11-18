package com.yiling.cms.question.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 疑问处理库
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 内容详情
     */
    private String content;

    /**
     * 所属分类：1-药品相关
     */
    private Integer categoryId;

    /**
     * 阅读量
     */
    private Long viewCount;

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
     * 最新回复时间
     */
    private Date lastReplyTime;

    /**
     * 问题库区分 1-问题知识库 2-医药代表社区库
     */
    private Integer type;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
