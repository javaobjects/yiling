package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 内容分页列表查询参数
 *
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryClientContentPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    @ApiModelProperty(value = "引用业务线id")
    @NotNull
    private Long lineId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 医生id
     */
    @ApiModelProperty(value = "医生id")
    private Long docId;

    /**
     * 状态：1-未发布 2-已发布
     */
    @ApiModelProperty(value = "状态：1-未发布 2-已发布")
    private Integer status;

    /**
     * 是否院方文章 0-否，1-是
     */
    @ApiModelProperty(value = "是否院方文章 0-否，1-是")
    private Integer ihFlag;

    /**
     * 置顶:1-是 0--否
     */
    @ApiModelProperty(value = "置顶:1-是 0--否")
    private Integer isTop;

    /**
     * 是否手动排序 0-否，1-是
     */
    @ApiModelProperty(value = "是否手动排序 0-否，1-是")
    private Integer isHandRank;

    /**
     * 类型:1-文章 2-视频
     */
    @ApiModelProperty(value = "类型:1-文章 2-视频")
    private Integer contentType;

}