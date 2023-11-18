package com.yiling.sjms.flow.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/2/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBalanceStatisticsForm extends QueryPageListForm {

    @ApiModelProperty("经销商编码")
    private Long crmEnterpriseId;

    @ApiModelProperty("开始时间")
    private Date startDateTime;

    @ApiModelProperty("结束时间")
    private Date endDateTime;

    @ApiModelProperty("经销商级别")
    private Integer enameLevel;

    @ApiModelProperty(value = "流向搜集方式")
    private Integer flowMode;
}
