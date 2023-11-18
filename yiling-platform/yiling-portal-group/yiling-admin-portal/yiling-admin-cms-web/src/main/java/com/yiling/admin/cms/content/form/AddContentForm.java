package com.yiling.admin.cms.content.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.admin.cms.content.vo.LineModuleVO;
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
public class AddContentForm extends BaseForm {

    // @ApiModelProperty(value = "栏目id")
    // @NotNull
    // private Long categoryId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    @NotEmpty
    private String title;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String subtitle;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面")
    private String cover;

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
    @ApiModelProperty(value = "置顶:1-是 0--否")
    private Integer isTop;

    /**
     * 状态 1未发布 2发布
     */
    @ApiModelProperty(value = "状态 1未发布 2立即发布")
    private Integer status;

    /**
     * 1-是 0-否
     */
    @ApiModelProperty(value = "草稿：1-是 0-否")
    private Integer isDraft;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "显示业务线")
    private List<Long> displayLines;

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

    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    /**
     * 创建来源 1-运营后台，2-IH后台
     */
    private Integer createSource = 1;

}
