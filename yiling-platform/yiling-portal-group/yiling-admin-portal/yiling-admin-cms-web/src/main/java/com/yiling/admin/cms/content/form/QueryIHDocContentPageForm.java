package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * IHDoc内容分页列表查询参数
 *
 * @author: fan.shen
 * @date: 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIHDocContentPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "引用业务线id", hidden = true)
    private Long lineId = 2L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "引用板块id")
    private Long moduleId;

    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    @ApiModelProperty(value = "类型:1-文章 2-视频")
    private Integer contentType;

    @ApiModelProperty(value = "是否手动排序 0-否，1-是")
    private Integer handRankFlag;

    @ApiModelProperty(value = "医生id")
    private Long docId;

    @ApiModelProperty(value = "状态：1-未发布 2-已发布")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建来源 1-CMS运营后台, 2-IH运营后台")
    private Integer createSource;

    @ApiModelProperty(value = "置顶:1-是 0--否")
    private Integer topFlag;


}