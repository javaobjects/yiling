package com.yiling.open.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/12/06
 */
@Data
@Accessors(chain = true)
public class PublishContentForm extends BaseForm {

    @ApiModelProperty("id")
    private Long id;

    /**
     * 置顶:1-是 0--否
     */
    @ApiModelProperty(value = "置顶:1-是 0--否", hidden = true)
    private Integer isTop;


    /**
     * 所属医生id
     */
    @ApiModelProperty("所属医生id")
    private Long docId;

    /**
     * 类型:1-文章 2-视频
     */
    @ApiModelProperty("类型:1-文章 2-视频")
    private Integer contentType;

    /**
     * 状态 1未发布 2发布
     */
    @ApiModelProperty("状态 1未发布 2发布")
    private Integer status;

    /**
     * 操作人id
     */
    @ApiModelProperty("操作人id")
    private Integer opUserId;
}
