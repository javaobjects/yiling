package com.yiling.admin.erp.enterprise.form;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/10/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpMonitorSaleExceptionPageForm extends QueryPageListForm {

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业ID")
    private Long    eid;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String  ename;

    /**
     * 销售单据时间，开始
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "销售单据时间，开始（最大查询范围31天）")
    private Date flowTimeStart;

    /**
     * 销售单据时间，结束
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "销售单据时间，结束（最大查询范围31天）")
    private Date flowTimeEnd;

    /**
     * 销售单主键ID
     */
    @ApiModelProperty(value = "销售单主键ID")
    private String soId;

    /**
     * 销售单号
     */
    @ApiModelProperty(value = "销售单号")
    private String soNo;

    /**
     * 任务上传ID
     */
    @ApiModelProperty(value = "任务上传ID")
    private Long controlId;

    /**
     * 父类企业ID
     */
    @ApiModelProperty(value = "父类企业ID")
    private Long parentId;

    /**
     * 查询类型：0-全部，1-关闭对接企业数量，2-24小时无心跳企业数量,3-当月未上传销售企业数量，4-销售异常数量，5-采购异常数量
     */
    @NotNull
    @Min(0)
    @Max(5)
    @ApiModelProperty(value = "查询类型")
    private Integer  openType;
}
