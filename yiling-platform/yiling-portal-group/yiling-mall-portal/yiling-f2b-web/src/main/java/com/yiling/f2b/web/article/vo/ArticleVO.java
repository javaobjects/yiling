package com.yiling.f2b.web.article.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文章信息 VO
 * @author fan.shen
 * @date 2021/12/27
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ArticleVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章描述")
    private String articleDesc;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("文章状态 1-可用，2-停用")
    private Integer articleStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建时间")
    private Date updateTime;

}
