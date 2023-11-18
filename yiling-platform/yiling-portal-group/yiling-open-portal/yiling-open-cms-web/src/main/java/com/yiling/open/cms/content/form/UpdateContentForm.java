package com.yiling.open.cms.content.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateContentForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;


    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    @NotEmpty
    @Length(max = 50)
    private String title;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    @Length(max = 50)
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
    @Length(max = 30)
    private String source;

    /**
     * 作者
     */
    @ApiModelProperty(value = "作者")
    private String author;

    /**
     * 置顶:1-是 0--否
     */
    @ApiModelProperty(value = "置顶:1-是 0--否", hidden = true)
    private Integer isTop;

    /**
     * 状态 1未发布 2发布
     */
    @ApiModelProperty(value = "状态 1未发布 2立即发布")
    private Integer status;

    /**
     * 1-是 0-否
     */
    @ApiModelProperty(value = "草稿：1-是 0-否", hidden = true)
    private Integer isDraft;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容详情")
    private String content;


    /**
     * 是否公开：0-否 1-是
     */
    @ApiModelProperty(value = "是否公开：0-否 1-是")
    private Integer isOpen;

    /**
     * 视频oss key
     */
    @ApiModelProperty(value = "视频oss key", hidden = true)
    private String vedioFileUrl;

    /**
     * 类型:1-文章 2-视频
     */
    @ApiModelProperty(value = "类型:1-文章 2-视频", hidden = true)
    private Integer contentType;

    /**
     * 会议id
     */
    @ApiModelProperty(value = "会议id", hidden = true)
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

    @ApiModelProperty(value = "主讲人", hidden = true)
    private String speaker;

    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    /**
     * 创建来源 1-运营后台，2-IH后台
     */
    @ApiModelProperty(value = "创建来源 1-运营后台，2-IH后台")
    private Integer createSource;

    /**
     * 操作人id
     */
    @ApiModelProperty("操作人id")
    private Integer opUserId;

    @NotEmpty
    @ApiModelProperty(value = "栏目集合")
    private List<AddOrUpdateContentCategoryForm> categoryList;

}
