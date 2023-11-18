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
public class QueryAccompanyingBillForm extends QueryPageListForm {


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String docCode;

    @ApiModelProperty(value = "审核状态 6-待审核 1-审核通过 3-审核驳回",required = true)
    private Integer auditStatus;


    private Date startTime;

    private Date endTime;

    /**
     * 发货名称
     */
    @ApiModelProperty(value = "发货单位")
    private String distributorEname;

    @ApiModelProperty(value = "提交人")
    private String createUserName;
 }