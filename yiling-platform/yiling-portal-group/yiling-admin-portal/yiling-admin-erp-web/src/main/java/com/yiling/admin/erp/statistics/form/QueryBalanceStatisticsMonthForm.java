package com.yiling.admin.erp.statistics.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QueryBalanceStatisticsMonthForm extends QueryPageListForm {

    @ApiModelProperty(value = "年月份(yyyy-MM)")
    private String time;

    @ApiModelProperty(value = "企业id")
    private Long eid;

    @ApiModelProperty(value = "实施负责人")
    private String installEmployee;

    @ApiModelProperty(value = "下拉类型(1-采购 2-销售 3-当前库存 4-平衡相差数)")
    private Integer quantityType;

    @ApiModelProperty(value = "开始数量")
    private Integer minQuantity;

    @ApiModelProperty(value = "结束数量")
    private Integer maxQuantity;
}
