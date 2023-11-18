package com.yiling.sjms.flow.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/2/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPageForm extends QueryPageListForm {

    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(value = "查询开始时间", required = true)
    private Date startTime;


    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(value = "查询结束时间", required = true)
    private Date endTime;

    @ApiModelProperty(value = "经销商级别 字典crm_supplier_level")
    private Integer supplierLevel;

    @ApiModelProperty("经销商编码")
    private Long crmEnterpriseId;

    @ApiModelProperty(value = "客户名称/供应商名称")
    private String enterpriseName;

    @ApiModelProperty(value = "原始商品名称")
    private String goodsName;

    @ApiModelProperty(value = "原始商品规格")
    private String goodsSpec;

    @ApiModelProperty(value = "数据标签查询列表（支持多选）")
    private List<Integer> dataTags;

}
