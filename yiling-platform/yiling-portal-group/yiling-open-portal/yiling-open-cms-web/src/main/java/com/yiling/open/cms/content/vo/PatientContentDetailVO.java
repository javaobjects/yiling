package com.yiling.open.cms.content.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PatientContentDetailVO extends BaseVO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("发布时间")
    private Date publishTime;

    @ApiModelProperty("栏目名称")
    private String categoryName;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("点赞数")
    private Long likeCount;

    @ApiModelProperty("点赞状态 1-点赞 2-未点赞")
    private Integer likeStatus;

    @ApiModelProperty("医生id")
    private Long docId;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
