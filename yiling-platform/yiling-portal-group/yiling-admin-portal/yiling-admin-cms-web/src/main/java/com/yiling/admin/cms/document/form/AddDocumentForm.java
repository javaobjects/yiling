package com.yiling.admin.cms.document.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加文献
 * @author: gxl
 * @date: 2022/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddDocumentForm extends BaseForm {

    /**
     * cms_document_category表id
     */
    @ApiModelProperty(value = "文献栏目id")
    private Long categoryId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 简述
     */
    @ApiModelProperty(value = "简述")
    private String resume;

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
     * 状态：1-未发布 2-已发布
     */
    @ApiModelProperty(value = "状态：1-未发布 2-已发布")
    private Integer status;

    @ApiModelProperty(value = "显示业务线")
    private List<Long> displayLines;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容概述")
    private String content;

    /**
     * 文献pdf oss key
     */
    @ApiModelProperty(value = "文献pdf oss key")
    private String documentFileUrl;

    @ApiModelProperty(value = "文献名称")
    private String documentFileName;


    /**
     * 是否公开：0-否 1-是
     */
    @ApiModelProperty(value = "是否公开：0-否 1-是")
    private Integer isOpen;



    /**
     *
     */
    List<Long> standardGoodsIdList;
}