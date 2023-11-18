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
public class AppContentDTO extends BaseDTO {

    private static final long serialVersionUID = 2823126310827112384L;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 点击量
     */
    private Integer pageView;

    /**
     * 2C用户侧浏览量
     */
    private Integer hmcView;

    /**
     * 发布时间
     */
    private Date publishTime;

    private Date updateTime;
    /**
     * 视频oss key
     */
    private String vedioFileUrl;

    private String speaker;

    /**
     * 类型:1-文章 2-视频
     */
    private Integer contentType;

    private Date createTime;

    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 内容权限 1-仅登录，2-需认证
     */
    private Integer contentAuth;

    /**
     * 内容来源:1-站内创建 2-外链
     */
    private Integer sourceContentType;

    /**
     * H5地址
     */
    private String linkUrl;
}
