package com.yiling.admin.cms.question.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionDelegateDetailVO extends BaseDTO {

    /**
     * 标题
     */
    @ApiModelProperty("疑义标题")
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

    @ApiModelProperty("是否展示回复按钮：1-展示 2- 不展示")
    private Integer showReplyButtonFlag = 2;

    /**
     *  标准库药品名称
     */
    @ApiModelProperty("标准库药品名称")
    private String name;

    /**
     *标准库药品ID
     */
    @ApiModelProperty("标准库药品ID")
    private Long standardId;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;

    /**
     * 规格ID
     */
    @ApiModelProperty("规格ID")
    private Long sellSpecificationsId;


    /**
     * 是否回复 1-未回复 2-已回复
     */
    @ApiModelProperty("是否回复 1-未回复 2-已回复 ")
    private Integer replyFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 图片信息
     */
    @ApiModelProperty("图片信息")
    private List<FileInfoVO> pictureList;

    /**
     * 回复内容
     */
    @ApiModelProperty("回复内容")
    private List<QuestionReplyDetailInfoVO> replyList;

}
