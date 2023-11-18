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
 * 回复详细信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionReplyDetailInfoVO extends BaseDTO {
    

    /**
     * 回复内容
     */
    @ApiModelProperty("回复内容")
    private String replyContent;

    /**
     * 回复关联文献ID
     */
    @ApiModelProperty("回复内容")
    private List<DocumentVO> replyDocumentList;

    /**
     * 回复关联链接地址
     */
    @ApiModelProperty("回复关联链接")
    private List<String> replyUrlList;

    /**
     * 回复文件关联
     */
    @ApiModelProperty("回复关联文件")
    private List<FileReplyVO> replyFileList;

    /**
     * 关联图片
     */
    @ApiModelProperty("回复关联图片")
    private List<FileInfoVO> replyPictureList;


    /**
     * 回复人ID
     */
    @ApiModelProperty("回复人ID")
    private Long createUser;

    /**
     * 回复时间
     */
    @ApiModelProperty("回复时间")
    private Date createTime;

    /**
     * 回复人名称
     */
    @ApiModelProperty("回复人名称")
    private String createUserName;

}
