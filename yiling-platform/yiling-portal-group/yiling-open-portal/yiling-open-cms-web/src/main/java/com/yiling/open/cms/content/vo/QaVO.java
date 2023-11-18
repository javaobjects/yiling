package com.yiling.open.cms.content.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: fan.shen
 * @data: 2023-03-14
 */
@Data
public class QaVO extends BaseVO {

    @ApiModelProperty("回复结果")
    private Integer qaType;

    @ApiModelProperty("cms_qa问答表主键")
    private Long qaId;

    @ApiModelProperty("问答内容")
    private String content;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("回复结果")
    private List<AnswerVO> answerList;
}
