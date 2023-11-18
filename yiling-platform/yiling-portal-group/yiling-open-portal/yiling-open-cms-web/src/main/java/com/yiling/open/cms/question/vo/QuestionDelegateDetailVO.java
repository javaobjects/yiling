package com.yiling.open.cms.question.vo;

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
     *  标准库药品名称
     */
    @ApiModelProperty("标准库药品名称")
    private String name;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;


    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 被提问医药代表名称
     */
    @ApiModelProperty("被提问医药代表名称")
    private String toUserName;

    /**
     * 被提问医药代表电话
     */
    @ApiModelProperty("被提问医药代表电话")
    private String mobile;

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
