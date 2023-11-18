package com.yiling.cms.content.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容
 * </p>
 *
 * @author gxl
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_content")
public class ContentDO extends BaseDO {

    private static final long serialVersionUID = 1L;
    //
    // /**
    //  * 所属前台分类
    //  */
    // private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 封面
     */
    private String cover;

    /**
     * 来源
     */
    private String source;

    /**
     * 类型:1-文章 2-视频
     */
    private Integer contentType;

    /**
     * 作者
     */
    private String author;

    /**
     * 内容来源:1-站内创建 2-外链
     */
    private Integer sourceContentType;

    /**
     * 视频oss key
     */
    private String vedioFileUrl;

    /**
     * H5地址
     */
    private String linkUrl;

    /**
     * 置顶:1-是 0--否
     */
    private Integer isTop;

    /**
     * 状态：1-未发布 2-已发布
     */
    private Integer status;

    /**
     * 立即发布：1-是 0-否
     */
    private Integer publishNow;

    /**
     * 点击量
     */
    private Integer pageView;

    /**
     * 2C用户侧浏览量
     */
    private Integer hmcView;

    /**
     * IH医生端浏览量
     */
    private Integer ihDocView;
    //
    // /**
    //  * IH患者端端浏览量
    //  */
    // private Integer ihPatientView;

    /**
     * 草稿：1-是 0-否
     */
    private Integer isDraft;

    /**
     * 会议id
     */
    private Long meetingId;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer isOpen;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 所属医生id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long docId;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 创建来源 1-运营后台，2-IH后台
     */
    private Integer createSource;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 主讲人
     */
    private String speaker;

    /**
     * 医生端查看权限1-仅登录 2-需认证通过
     */
    private Integer viewLimit;
}
