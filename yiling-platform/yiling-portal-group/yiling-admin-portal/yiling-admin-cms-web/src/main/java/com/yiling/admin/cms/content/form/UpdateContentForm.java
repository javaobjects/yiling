package com.yiling.admin.cms.content.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

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
public class UpdateContentForm extends BaseForm {

    @NotNull
    private Long id;

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
     * 作者
     */
    private String author;

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

    /**
     * 置顶:1-是 0--否
     */
    private Integer isTop;

    /**
     * 1-是 0-否
     */
    private Integer publishNow;

    /**
     * 1-是 0-否
     */
    private Integer isDraft;

    /**
     * 内容
     */
    private String content;


    // private List<Long> displayLines;

    /**
     * 状态 1未发布 2发布
     */
    @ApiModelProperty(value = "状态 1未发布 2发布")
    private Integer status;

    /**
     * 是否公开：0-否 1-是
     */
    @ApiModelProperty(value = "是否公开：0-否 1-是")
    private Integer isOpen;

    /**
     * 视频oss key
     */
    @ApiModelProperty(value = "视频oss key")
    private String vedioFileUrl;

    /**
     * 类型:1-文章 2-视频
     */
    @ApiModelProperty(value = "类型:1-文章 2-视频")
    @NotNull
    private Integer contentType;

    /**
     * 会议id
     */
    @ApiModelProperty(value = "会议id")
    private Long meetingId;

    /**
     * 关联标准库商品
     */
    @ApiModelProperty(value = "关联标准库商品")
    private List<Long> standardGoodsIdList;
    /**
     * 科室
     */
    @ApiModelProperty(value = "科室")
    private List<Long> deptIdList;
    /**
     * 疾病
     */
    @ApiModelProperty(value = "疾病")
    private List<Long> diseaseIdList;

    @ApiModelProperty(value = "主讲人")
    private String speaker;

    @ApiModelProperty(value = "内容权限 1-仅登录 2-需认证通过")
    private Integer viewLimit;

    /**
     * 所属医生id
     */
    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    @ApiModelProperty(value = "业务线模块设置")
    List<LineModuleForm> lineModuleList;


}
