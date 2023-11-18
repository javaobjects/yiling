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
 * @date: 2022/10/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpMonitorPurchaseExceptionPageForm extends QueryPageListForm {

    /**
     * 采购企业id
     */
    @ApiModelProperty(value = "采购企业id")
    private Long eid;

    /**
     * 采购企业名称
     */
    @ApiModelProperty(value = "采购企业名称")
    private String ename;

    /**
     * 采购单据时间，开始
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "采购单据时间，开始")
    private Date poTimeStart;

    /**
     * 采购单据时间，结束
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "采购单据时间，结束")
    private Date poTimeEnd;

    /**
     * 查询类型：0-全部，1-关闭对接企业数量，2-24小时无心跳企业数量,3-当月未上传销售企业数量，4-销售异常数量，5-采购异常数量
     */
    @NotNull
    @Min(0)
    @Max(5)
    @ApiModelProperty(value = "查询类型")
    private Integer  openType;
}
