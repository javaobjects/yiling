package com.yiling.admin.data.center.report.form;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@Data
@Accessors(chain = true)
public class QueryReportPricePageForm extends QueryPageListRequest {

    private static final long serialVersionUID = 5693047778898532308L;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", required = false)
    private String goodsName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = false)
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = false)
    private Date endTime;

    @ApiModelProperty(value = "参数id", required = true)
    private Long paramId;

    private String optName;
}