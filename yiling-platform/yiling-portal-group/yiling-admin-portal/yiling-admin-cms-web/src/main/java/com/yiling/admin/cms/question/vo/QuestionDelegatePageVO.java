package com.yiling.admin.cms.question.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
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
public class QuestionDelegatePageVO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 问题标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 内容详情
     */
    @ApiModelProperty("内容详情")
    private String content;

    /**
     * 提问人名称
     */
    @ApiModelProperty("提问人名称")
    private String fromUserName;

    /**
     * 提问人id
     */
    @ApiModelProperty("提问人id")
    private Long fromUserId;

    /**
     * 被提问医药代表名称
     */
    @ApiModelProperty("被提问医药代表名称")
    private String toUserName;

    /**
     * 被提问医药代表名称id
     */
    @ApiModelProperty("被提问医药代表名称id")
    private Long toUserId;

    /**
     * 是否回复 1-未回复 2-已回复 3-不需要回复
     */
    @ApiModelProperty("是否回复 1-未回复 2-已回复 ")
    private Integer replyFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 是否展示恢复按钮 0-展示 1- 不展示
     */
    @ApiModelProperty("是否展示回复按钮：1-展示 2- 不展示")
    private Integer showReplyButtonFlag = 2;

    /**
     * 图片
     */
    @ApiModelProperty("图片信息")
    private  List<FileInfoVO> fileList;

    /**
     *  标准库药品名称
     */
    @ApiModelProperty("标准库药品名称")
    private String name;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;


}
