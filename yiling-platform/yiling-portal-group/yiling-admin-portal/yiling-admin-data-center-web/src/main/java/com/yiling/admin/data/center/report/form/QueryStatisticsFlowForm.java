package com.yiling.admin.data.center.report.form;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStatisticsFlowForm extends BaseRequest {
    /**
     * 商品品类:0连花1非连花
     */
    @ApiModelProperty(value = "商品品类:1连花2非连花")
    private Integer goodsCategory;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "商业公司编码")
    private Long eid;

    @ApiModelProperty(value = "渠道Id", example = "1")
    private Long channelId;

    @ApiModelProperty(value = "是否云仓标识0全部1云仓",example = "1")
    private Integer isCloudFlag;

}
