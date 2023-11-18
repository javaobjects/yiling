package com.yiling.admin.data.center.report.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@Data
@Accessors(chain = true)
public class QueryParamSubGoodsPageListForm extends QueryPageListForm {

    /**
     * 子参数id
     */
    @NotNull
    @ApiModelProperty(value = "子参数id")
    private Long paramSubId;

    /**
     * 商业id
     */
    @ApiModelProperty("商业id")
    private Long eid;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String userName;
}