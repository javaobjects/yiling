package com.yiling.sjms.wash.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Data
public class QueryFlowPurchaseWashPageForm extends QueryPageListForm {

    @NotNull(message = "流向任务清洗id不可为空")
    @ApiModelProperty(value = "流向任务清洗id", required = true)
    private Long fmwtId;

    @ApiModelProperty(value = "行业库经销商编码")
    private Long crmEnterpriseId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "供应商名称")
    private String enterpriseName;

    @ApiModelProperty(value = "原始商品名称")
    private String goodsName;

    @ApiModelProperty(value = "原始商品规格")
    private String goodsSpec;

    @ApiModelProperty(value = "标准商品")
    private Long crmGoodsCode;

    @ApiModelProperty(value = "流向清洗对照状态 字典：flow_data_wash_mapping_status 1-两者都未匹配 2-商品未匹配 3-客户未匹配 4-匹配成功")
    private Integer mappingStatus;

    @ApiModelProperty(value = "流向清洗明细结果 字典：flow_data_wash_status 1-未清洗 2-正常 3-疑似重复 4-区间外删除")
    private Integer washStatus;
}
