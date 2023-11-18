package com.yiling.admin.sales.assistant.task.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 随货同行单列表查询参数
 * @author: gxl
 * @date: 2023/1/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMatchBillPageForm extends QueryPageListForm {


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String docCode;

    /**
     * 核对结果
     */
    @ApiModelProperty(value = "字典accompany_bill_match_status")
    private Integer result;
    /**
     * 收货企业名称
     */
    @ApiModelProperty(value = "收货企业名称")
    private String recvEname;
    /**
     * erp获取流向时间 开始
     */
    @ApiModelProperty(value = "erp获取流向时间 开始")
    private Date erpMatchStartTime;
    /**
     * erp获取流向时间 结束
     */
    @ApiModelProperty(value = "erp获取流向时间 结束")
    private Date erpMatchEndTime;
    /**
     * crm获取流向时间 开始
     */
    @ApiModelProperty(value = "单据编号")
    private Date crmMatchStartTime;
    /**
     * crm获取流向时间 结束
     */
    @ApiModelProperty(value = "单据编号")
    private Date crmMatchEndTime;

}