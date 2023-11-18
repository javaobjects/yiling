package com.yiling.cms.content.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class AppContentDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 7783990059611469007L;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 2C用户侧浏览量
     */
    private Integer hmcView;

    /**
     * IH医生端浏览量
     */
    private Integer ihDocView;

    /**
     * 点击量
     */
    private Integer pageView;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private String content;

    /**
     * 视频oss key
     */
    private String vedioFileUrl;

    /**
     * 主讲人
     */
    private String speaker;

    private Date createTime;

    private String cover;

    private Date updateTime;

    private Integer viewLimit;


    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 内容来源:1-站内创建 2-外链
     */
    private Integer sourceContentType;

    /**
     * H5地址
     */
    private String linkUrl;

}
