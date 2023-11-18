package com.yiling.admin.pop.banner.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询banner分页列表 Form
 *
 * @author: yuecheng.yue
 * @date: 2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBannerPageListForm extends QueryPageListForm {


    /**
     * banner标题
     */
    @ApiModelProperty(value = "banner标题")
    private String title;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

    /**
     * 查询创建开始时间
     */
    @ApiModelProperty(value = "查询创建开始时间")
    private Date createTimeStart;

    /**
     * 查询创建结束时间
     */
    @ApiModelProperty(value = "查询创建结束时间")
    private Date createTimeEnd;

}
