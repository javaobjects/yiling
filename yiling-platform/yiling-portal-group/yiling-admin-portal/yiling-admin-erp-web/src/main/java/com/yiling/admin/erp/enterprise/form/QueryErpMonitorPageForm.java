package com.yiling.admin.erp.enterprise.form;

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
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpMonitorPageForm extends QueryPageListForm {

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业ID")
    private Long    rkSuId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String  clientName;

    /**
     * 对接负责人
     */
    @ApiModelProperty(value = "对接负责人")
    private String  installEmployee;

    /**
     * 查询类型：0-全部，1-关闭对接企业数量，2-24小时无心跳企业数量,3-当月未上传销售企业数量，4-销售异常数量，5-采购异常数量
     */
    @NotNull
    @Min(0)
    @Max(5)
    @ApiModelProperty(value = "查询类型")
    private Integer  openType;

}
