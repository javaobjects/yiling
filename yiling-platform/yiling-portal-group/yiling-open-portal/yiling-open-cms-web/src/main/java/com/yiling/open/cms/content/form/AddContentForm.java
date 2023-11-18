package com.yiling.open.cms.content.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

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
public class AddContentForm extends BaseForm {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 副标题
     */
    @ApiModelProperty("副标题")
    private String subtitle;

    /**
     * 封面
     */
    @ApiModelProperty("封面")
    private String cover;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private String author;

    /**
     * 置顶:1-是 0--否
     */
    @ApiModelProperty("置顶:1-是 0--否")
    private Integer isTop;

    /**
     * 状态 1未发布 2发布
     */
    @ApiModelProperty("状态 1未发布 2发布")
    private Integer status;

    /**
     * 草稿 1-是 0-否
     */
    @ApiModelProperty("草稿 1-是 0-否")
    private Integer isDraft;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;


    /**
     * 是否公开：0-否 1-是
     */
    @ApiModelProperty(" 是否公开：0-否 1-是")
    private Integer isOpen;

    /**
     * 视频oss key
     */
    @ApiModelProperty("视频oss key")
    private String vedioFileUrl;

    /**
     * 类型:1-文章 2-视频
     */
    @ApiModelProperty("类型:1-文章 2-视频")
    private Integer contentType;

    /**
     * 会议id
     */
    @ApiModelProperty("会议id")
    private Long meetingId;

    /**
     * 关联标准库商品
     */
    @ApiModelProperty("关联标准库商品")
    private List<Long> standardGoodsIdList;

    /**
     * 科室
     */
    @ApiModelProperty("科室")
    private List<Long> deptIdList;

    /**
     * 疾病
     */
    @ApiModelProperty("疾病")
    private List<Long> diseaseIdList;

    /**
     * 主讲人
     */
    @ApiModelProperty("主讲人")
    private String speaker;

    /**
     * 所属医生id
     */
    @ApiModelProperty("所属医生id")
    private Long docId;

    /**
     * 创建来源 1-运营后台，2-IH后台
     */
    @ApiModelProperty("创建来源 1-运营后台，2-IH后台")
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
