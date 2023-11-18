package com.yiling.admin.erp.statistics.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSpecStatisticsForm extends QueryPageListForm {

    @ApiModelProperty("企业id")
    @NotNull(message = "企业id必传")
    private Long eid;

    @ApiModelProperty("查询月份 yyyy-MM")
    @NotEmpty(message = "年月必传")
    private String monthTime;

    @ApiModelProperty("商品+规格id")
    private Long specificationId;

}
