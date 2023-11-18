package com.yiling.sales.assistant.app.content.vo;

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
public class ContentVO extends BaseVO {


    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面")
    private String cover;

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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "类型:1-文章 2-视频")
    private String contentType;

    @ApiModelProperty(value = "视频oss key")
    private String vedioFileUrl;

    /**
     * 业务线id
     */
    @ApiModelProperty(value = "业务线id")
    private Long lineId;

    /**
     * 模块id
     */
    @ApiModelProperty(value = "模块id")
    private Long moduleId;

    /**
     * 栏目id
     */
    @ApiModelProperty(value = "栏目id")
    private Long categoryId;


    /**
     * 内容id
     */
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

    @ApiModelProperty(value = "主讲人")
    private String speaker;
}
