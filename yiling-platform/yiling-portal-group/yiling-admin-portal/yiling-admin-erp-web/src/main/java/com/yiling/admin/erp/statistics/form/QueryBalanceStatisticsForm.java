package com.yiling.admin.erp.statistics.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBalanceStatisticsForm extends BaseForm {

    @ApiModelProperty("企业id")
    private Long eid;

    @ApiModelProperty("年月份(yyyy-MM)")
    private String monthTime;
}
