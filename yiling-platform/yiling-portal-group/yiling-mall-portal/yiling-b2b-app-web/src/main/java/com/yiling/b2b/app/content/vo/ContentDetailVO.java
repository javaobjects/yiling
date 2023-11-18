package com.yiling.b2b.app.content.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ContentDetailVO extends BaseVO {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String subtitle;

    /**
     * 点击量
     */
    @ApiModelProperty(value = "点击量")
    private Integer pageView;


    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;


    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String source;

    /**
     * 作者
     */
    @ApiModelProperty(value = "作者")
    private String author;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 视频oss key
     */
    @ApiModelProperty(value = "视频oss key")
    private String vedioFileUrl;

    @ApiModelProperty(value = "")
    private String speaker;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "")
    private Integer viewLimit;

    @ApiModelProperty(value = "收藏状态：1-收藏 2-取消收藏")
    private Integer collectStatus;

    @ApiModelProperty(value = "内容id")
    private Long contentId;

    /**
     * 内容来源:1-站内创建 2-外链
     */
    @ApiModelProperty(value = "内容来源:1-站内创建 2-外链")
    private Integer sourceContentType;

    /**
     * H5地址
     */
    @ApiModelProperty(value = "H5地址")
    private String linkUrl;
}
